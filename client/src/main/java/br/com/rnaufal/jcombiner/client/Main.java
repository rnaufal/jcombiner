package br.com.rnaufal.jcombiner.client;

import br.com.rnaufal.jcombiner.api.JCombiner;
import br.com.rnaufal.jcombiner.client.domain.People;
import br.com.rnaufal.jcombiner.client.domain.Person;
import br.com.rnaufal.jcombiner.client.service.PeopleCombinationService;

import java.util.ServiceLoader;

/**
 * Created by rnaufal
 */
public class Main {

    public static void main(String[] args) {
        final var people = new People();
        people.addMen(new Person("John"));
        people.addMen(new Person("Paul"));
        people.addMen(new Person("Patrick"));
        people.addMen(new Person("Joe"));
        people.addMen(new Person("Thomas"));

        people.addWoman(new Person("Anna"));
        people.addWoman(new Person("Emma"));
        people.addWoman(new Person("Lisa"));
        people.addWoman(new Person("Janet"));

        final var peopleCombinationService = new PeopleCombinationService(ServiceLoader.load(JCombiner.class));

        System.out.println(peopleCombinationService.from(people));
    }
}
