package optimization.cooridate.descent;

import optimization.cooridate.descent.graph.LineDrawer;
import optimization.cooridate.descent.graph.StripLine;
import org.apache.commons.lang3.tuple.Pair;
import statistic.modeling.lab1.SimpleDrawerImprove;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static optimization.cooridate.descent.CoefficientPack.SEMEN;

/**
 * Polytech
 * Created by igor on 11.04.17.
 */
public class DescentMain {
    private static final double SIZE = 10.0;
    public static void main(final String[] args) {
        final CoefficientPack coefficientPack = SEMEN;
        final SimpleDrawerImprove simpleDrawerImprove = new SimpleDrawerImprove("Спуск");
        final LineDrawer lineDrawer = new LineDrawer();

        final StripLine ox = new StripLine(
                Arrays.asList(
                        Pair.of(-SIZE,0.0),
                        Pair.of(SIZE,0.0)
                )
        ),
                oy = new StripLine(
                        Arrays.asList(
                                Pair.of(0.0,-SIZE),
                                Pair.of(0.0,SIZE)
                        )
                );

        ox.setColor(Color.RED);
        oy.setColor(Color.RED);

        lineDrawer.addLine(ox);
        lineDrawer.addLine(oy);
        /*Igor*/
        final double a1 = coefficientPack.getA1();
        final double a2 = coefficientPack.getA2();
        final double a3 = coefficientPack.getA3();
        final double b1 = coefficientPack.getB1();
        final double b2 = coefficientPack.getB2();
        final double x0 = coefficientPack.getX0();
        final double y0 = coefficientPack.getY0();
        /**/

        List<Pair<Double, Double>> pairs;

        final Function<Pair<Double, Double>, Double> mainFunction =
                doubleDoublePair -> a1 * doubleDoublePair.getLeft() * doubleDoublePair.getLeft() +
                        a2 * doubleDoublePair.getLeft() * doubleDoublePair.getRight() +
                        a3 * doubleDoublePair.getRight() * doubleDoublePair.getRight() +
                        b1 * doubleDoublePair.getLeft() +
                        b2 * doubleDoublePair.getRight();

        final CoordinateDescent coordinateDescent = new CoordinateDescent(
                pair ->
                        a1 * pair.getLeft() * pair.getLeft() +
                                a2 * pair.getLeft() * pair.getRight() +
                                a3 * pair.getRight() * pair.getRight() +
                                b1 * pair.getLeft() +
                                b2 * pair.getRight()
                ,
                x -> -(a2 * x + b1) / 2 / a1,
                x -> -(a2 * x + b2) / 2 / a3,
                Pair.of(coefficientPack.getX0(), coefficientPack.getY0())
        );

        System.out.println("Coordinate descent");
        final Pair<Double, Double> realAnswer = coordinateDescent.make();
        final Pair<Double, Double> oneStepAnswer = coordinateDescent.oneStepPoint();

        pairs = coordinateDescent.getPairs();

        final StripLine first = new StripLine(pairs);
        first.setColor(Color.BLUE);

        lineDrawer.addLine(first);

        simpleDrawerImprove.addChartPanel(
                pairs.stream().map(Pair::getLeft).collect(Collectors.toList()),
                pairs.stream().map(Pair::getRight).collect(Collectors.toList())
        );

        System.out.println("Rosenbrok descent");
        final RosenbrokDescent rosenbrokDescent = new RosenbrokDescent(
                mainFunction,
                pair -> -(a2 * pair.getRight() + 2 * a3 * pair.getLeft() * pair.getRight() + b1 + pair.getLeft() * b2) * 0.5 /
                        (a1 + pair.getLeft() * a2 + a3 * pair.getLeft() * pair.getLeft()),
                Pair.of(x0, y0),
                oneStepAnswer,
                realAnswer
        );

        final Pair<Double, Double> idealLineAnswer = rosenbrokDescent.make();

        pairs = rosenbrokDescent.getPairs();

        final StripLine second = new StripLine(pairs);
        second.setColor(Color.GREEN);

        lineDrawer.addLine(second);

        simpleDrawerImprove.addChartPanel(
                pairs.stream().map(Pair::getLeft).collect(Collectors.toList()),
                pairs.stream().map(Pair::getRight).collect(Collectors.toList())
        );

        final CommonCoordinateDescent commonCoordinateDescent
                = new CommonCoordinateDescent(
                Pair.of(-4.0, -3.0),
                quartet -> -(2.0 * a1 * quartet.getX1() * quartet.getU1() +
                        a2 * quartet.getU1() * quartet.getX2() +
                        a2 * quartet.getU2() * quartet.getX1() +
                        2 * a3 * quartet.getU2() * quartet.getX2() +
                        b1 * quartet.getU1() + b2 * quartet.getU2()
                ) / (2 * a1 * quartet.getU1() * quartet.getU1() +
                        2 * a2 * quartet.getU1() * quartet.getU2() +
                        2 * a3 * quartet.getU2() * quartet.getU2()
                ),
                mainFunction
        );

        commonCoordinateDescent.make(idealLineAnswer.getLeft(), idealLineAnswer.getRight());

        pairs = commonCoordinateDescent.getPairs();

        final StripLine third = new StripLine(pairs);
        third.setColor(Color.ORANGE);

        lineDrawer.addLine(third);
        //simpleDrawerImprove.draw();

        /*SwingUtilities.invokeLater(() -> {
            new CGTemplate(lineDrawer); // Let the constructor do the job
        });*/
    }
}
