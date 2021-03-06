package technopolis.classes.impl;

import technopolis.classes.interfaces.Function;

/**
 * Polytech
 * Created by igor on 31.03.17.
 */
public class Divide extends BaseFunction {
    public Divide(final Function first, final Function second) {
        super(first,second);
    }

    @Override
    public double eval(final double x) {
        return first.eval(x) / second.eval(x);
    }

    @Override
    public double eval() {
        return withMinus*(first.eval() / second.eval());
    }
}
