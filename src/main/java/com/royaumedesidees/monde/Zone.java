package com.royaumedesidees.monde;

/**
 * Les quatre quarts de l'île. La frontière entre deux zones est une ligne nette (x = 0 ou z = 0),
 * même si le relief, lui, passe de l'une à l'autre en douceur.
 */
public enum Zone {
    /** Nord-ouest (x &lt; 0, z &lt; 0). */
    JARDIN_MILAN,
    /** Nord-est (x &ge; 0, z &lt; 0). */
    PORT_ROYAL,
    /** Sud-est (x &ge; 0, z &ge; 0). */
    PUY_DE_DOME,
    /** Sud-ouest (x &lt; 0, z &ge; 0). */
    HIPPONE;

    public static Zone en(int x, int z) {
        if (z < 0) {
            return x < 0 ? JARDIN_MILAN : PORT_ROYAL;
        }
        return x < 0 ? HIPPONE : PUY_DE_DOME;
    }
}
