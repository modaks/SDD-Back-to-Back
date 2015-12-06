import com.example.root.myapplication.WebServiceAdaptor;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void ParserTestReturnsTrue() {
        assertThat(WebServiceAdaptor.parser("u'merchant': u'Bankelok', u'category': [uWomen's\", u\"Women's > Shirts & Tops\", u\"Women's > Shirts & Tops\"], u'description': u'Agate Olive; Gold Plated\r\n', u'url': u'http://www.bankelok.com/collections/fineline/products/bindi-agate-olive-necklace', u'image': [u'http://cdn.shopify.com/s/files/1/0639/8619/products/Kt.Bindi-27_Site_Size.jpg?v=1420555037'], u'on_sale': False, u'price': 89, u'part_number': u'31566-FLNBO1', u'_id': ObjectId('5636b8ef760a4437c03e8944'), u'brand': u'Fineline', u'name': u'Bindi Agate Olive Necklace'").length==13);
    }

}