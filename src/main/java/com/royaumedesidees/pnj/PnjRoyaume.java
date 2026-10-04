package com.royaumedesidees.pnj;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base commune des PNJ du Royaume (Augustin jeune, Adéodat, Ambroise, Monique, Pascal, puis ceux des versions
 * suivantes). Un PNJ :
 * <ul>
 *   <li>ne disparaît jamais et reste autour de sa maison (un point et un rayon, sauvegardés) ;</li>
 *   <li>regarde les joueurs proches, et affiche toujours son nom ;</li>
 *   <li>parle dans une boîte de dialogue en haut de l'écran (pas dans le chat), son nom en couleur, avec des
 *       répliques traduisibles ({@code pnj.royaumedesidees.<id>.<réplique>}) ;</li>
 *   <li>est invulnérable : un coup de joueur ne lui fait rien, mais il réagit à sa façon ({@link #reagirCoup}).
 *       Seul {@code /kill} l'atteint. En v0.4, des PNJ « mortels » ({@link #estMortel}) donneront de la Culpabilité.</li>
 * </ul>
 */
public class PnjRoyaume extends PathfinderMob {
    /** Délai minimal entre deux réactions à des coups du même joueur (2 s). */
    private static final int DELAI_REACTION = 40;

    private final ChatFormatting couleur;
    private BlockPos maison;
    private int rayonMaison = 8;
    private final Map<UUID, Long> derniersCoups = new HashMap<>();

    protected PnjRoyaume(EntityType<? extends PnjRoyaume> type, Level niveau, ChatFormatting couleur) {
        super(type, niveau);
        this.couleur = couleur;
        setPersistenceRequired();
        // Le nom (traduit) au-dessus de la tête : le jeu ne l'affiche que pour un nom « personnalisé ».
        setCustomName(type.getDescription());
        for (EquipmentSlot emplacement : EquipmentSlot.values()) {
            setDropChance(emplacement, 0.0F);
        }
    }

    public static AttributeSupplier.Builder attributs() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 0.6));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /** Identifiant du PNJ (« pascal », « ambroise »…), tiré de son type d'entité. */
    public String id() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(getType()).getPath();
    }

    public ChatFormatting couleur() {
        return couleur;
    }

    /** Fixe la maison du PNJ : il y reste, dans le rayon donné. */
    public void setMaison(BlockPos centre, int rayon) {
        this.maison = centre.immutable();
        this.rayonMaison = rayon;
        restrictTo(maison, rayon);
    }

    public BlockPos maison() {
        return maison;
    }

    public int rayonMaison() {
        return rayonMaison;
    }

    /** Objet tenu en main, qui ne tombe jamais (le livre d'Ambroise, la poire d'Augustin…). */
    protected void tenir(ItemStack objet) {
        setItemSlot(EquipmentSlot.MAINHAND, objet);
    }

    /** Faux pour un PNJ qui n'a pas de maison et suit quelqu'un (Monique). */
    protected boolean aUneMaison() {
        return true;
    }

    /** Vrai pour les PNJ qu'on pourra tuer (v0.4) ; ceux de la v0.3 sont tous invulnérables. */
    public boolean estMortel() {
        return false;
    }

    // ------------------------------------------------------------------ Paroles

    /** Une réplique, précédée du nom du PNJ en couleur, pour le chat (annonces au serveur). */
    public MutableComponent ligne(String replique, Object... arguments) {
        return Component.empty()
                .append(getName().copy().withStyle(couleur, ChatFormatting.BOLD))
                .append(Component.literal(" : ").withStyle(couleur))
                .append(Component.translatable("pnj.royaumedesidees." + id() + "." + replique, arguments));
    }

    /** Une réplique dans la boîte de dialogue du joueur (voir {@link ParolePaquet}). */
    public void parler(ServerPlayer joueur, String replique, Object... arguments) {
        parlerPendant(joueur, 0, replique, arguments);
    }

    /** Une réplique qui reste affichée {@code ticks} ticks : une question à laquelle il faut répondre. */
    public void parlerPendant(ServerPlayer joueur, int ticks, String replique, Object... arguments) {
        PacketDistributor.sendToPlayer(joueur, new ParolePaquet(nom(),
                Component.translatable("pnj.royaumedesidees." + id() + "." + replique, arguments), ticks));
    }

    /** Une réplique pour tous les joueurs à portée de voix. */
    public void parlerAlentour(String replique, double rayon, Object... arguments) {
        if (level() instanceof ServerLevel monde) {
            for (ServerPlayer joueur : monde.players()) {
                if (joueur.distanceToSqr(this) <= rayon * rayon) {
                    parler(joueur, replique, arguments);
                }
            }
        }
    }

    /** Le nom du PNJ, en gras et dans sa couleur. */
    public MutableComponent nom() {
        return getName().copy().withStyle(couleur, ChatFormatting.BOLD);
    }

    /** Clic droit d'un joueur : à redéfinir par chaque PNJ (quêtes, défis). Par défaut, il salue. */
    protected void parleAvec(ServerPlayer joueur) {
        parler(joueur, "bonjour");
    }

    /** Réaction à un coup de joueur : à redéfinir. Le coup, lui, ne fait jamais de dégâts. */
    protected void reagirCoup(ServerPlayer joueur) {
        parler(joueur, "coup");
    }

    @Override
    protected InteractionResult mobInteract(Player joueur, InteractionHand main) {
        if (main == InteractionHand.MAIN_HAND && joueur instanceof ServerPlayer serveur) {
            getLookControl().setLookAt(joueur);
            parleAvec(serveur);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean hurt(DamageSource source, float degats) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || estMortel()) {
            return super.hurt(source, degats);
        }
        if (!level().isClientSide && source.getEntity() instanceof ServerPlayer joueur) {
            long maintenant = level().getGameTime();
            Long dernier = derniersCoups.get(joueur.getUUID());
            if (dernier == null || maintenant - dernier >= DELAI_REACTION) {
                derniersCoups.put(joueur.getUUID(), maintenant);
                getLookControl().setLookAt(joueur);
                reagirCoup(joueur);
            }
        }
        return false;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag donnees) {
        super.addAdditionalSaveData(donnees);
        if (maison != null) {
            donnees.put("Maison", NbtUtils.writeBlockPos(maison));
            donnees.putInt("RayonMaison", rayonMaison);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag donnees) {
        super.readAdditionalSaveData(donnees);
        NbtUtils.readBlockPos(donnees, "Maison").ifPresent(pos -> setMaison(pos, donnees.getInt("RayonMaison")));
    }

    @Override
    public void tick() {
        super.tick();
        // Un PNJ invoqué sans maison prend pour maison l'endroit où il apparaît.
        if (maison == null && aUneMaison() && !level().isClientSide) {
            setMaison(blockPosition(), rayonMaison);
        }
    }
}
