import br.com.rnaufal.jcombiner.JCombinerImpl;
import br.com.rnaufal.jcombiner.api.JCombiner;

/**
 * Created by rnaufal
 */
module impl {

    requires api;
    requires guava;
    requires org.apache.commons.collections4;
    requires commons.lang3;
    provides JCombiner with JCombinerImpl;
}