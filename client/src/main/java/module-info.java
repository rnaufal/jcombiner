import br.com.rnaufal.jcombiner.api.JCombiner;

/**
 * Created by rnaufal
 */
module client {
    requires api;

    opens br.com.rnaufal.jcombiner.client.domain;

    uses JCombiner;
}