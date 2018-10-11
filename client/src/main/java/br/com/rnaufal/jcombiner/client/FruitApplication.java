package br.com.rnaufal.jcombiner.client;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.client.domain.Fruit;
import br.com.rnaufal.jcombiner.client.domain.Fruits;
import br.com.rnaufal.jcombiner.client.service.FruitCombinationService;

import java.util.ServiceLoader;

/**
 * Created by rnaufal
 */
public class FruitApplication {

    public static void main(String[] args) {
        final var fruits = new Fruits();
        fruits.addRed(new Fruit("Red Apples"));
        fruits.addRed(new Fruit("Cherries"));
        fruits.addRed(new Fruit("Strawberries"));

        fruits.addYellow(new Fruit("Bananas"));
        fruits.addYellow(new Fruit("Oranges"));
        fruits.addYellow(new Fruit("Pineapple"));

        fruits.addGreen(new Fruit("Avocados"));
        fruits.addGreen(new Fruit("Limes"));
        fruits.addGreen(new Fruit("Green Grapes"));

        final var fruitsCombinationService = new FruitCombinationService(ServiceLoader.load(JCombiner.class));

        System.out.println(fruitsCombinationService.buildCombinations(fruits));
    }
}
