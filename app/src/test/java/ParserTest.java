import com.example.root.myapplication.WebServiceAdaptor;
import com.example.root.myapplication.Clothing;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void ParserTestReturnsTrue() {
        assertThat(WebServiceAdaptor.parser("u'merchant': u'Bankelok', u'category': [uWomen's\", u\"Women's > Shirts & Tops\", u\"Women's > Shirts & Tops\"], u'description': u'Agate Olive; Gold Plated\r\n', u'url': u'http://www.bankelok.com/collections/fineline/products/bindi-agate-olive-necklace', u'image': [u'http://cdn.shopify.com/s/files/1/0639/8619/products/Kt.Bindi-27_Site_Size.jpg?v=1420555037'], u'on_sale': False, u'price': 89, u'part_number': u'31566-FLNBO1', u'_id': ObjectId('5636b8ef760a4437c03e8944'), u'brand': u'Fineline', u'name': u'Bindi Agate Olive Necklace'").length==13);
    }

    public void ClothingClassTest(){
        Clothing test_clothing = new Clothing("Maker", "Type", "Description", "UrlShop", "UrlImage",
                "OnSale", "Price", "PartNumber", "ObjectID", "Brand", "Name");

        // Check clothing values
        assertEquals(test_clothing.getMaker().compareTo("Maker"), 0);
        assertEquals(test_clothing.getType().compareTo("Type"), 0);
        assertEquals(test_clothing.getDescription().compareTo("Description"), 0);
        assertEquals(test_clothing.getUrlShop().compareTo("UrlShop"), 0);
        assertEquals(test_clothing.getUrlImage().compareTo("UrlImage"), 0);
        assertEquals(test_clothing.getOnSale().compareTo("OnSale"), 0);
        assertEquals(test_clothing.getPrice().compareTo("Price"), 0);
        assertEquals(test_clothing.getPartNumber().compareTo("PartNumber"), 0);
        assertEquals(test_clothing.getObjectID().compareTo("ObjectID"), 0);
        assertEquals(test_clothing.getBrand().compareTo("Brand"), 0);
        assertEquals(test_clothing.getName().compareTo("Name"), 0);

        // Update values
        test_clothing.updateMaker("Maker_Maker");
        test_clothing.updateType("Type_Type");
        test_clothing.updateDescription("Description_Description");
        test_clothing.updateUrlShop("UrlShop_UrlShop");
        test_clothing.updateUrlImage("UrlImage_UrlImage");
        test_clothing.updateOnSale("OnSale_OnSale");
        test_clothing.updatePrice("Price_Price");
        test_clothing.updatePartNumber("PartNumber_PartNumber");
        test_clothing.updateObjectID("ObjectID_ObjectID");
        test_clothing.updateBrand("Brand_Brand");
        test_clothing.updateName("Name_Name");

        // Check new clothing values
        assertEquals(test_clothing.getMaker().compareTo("Maker_Maker"), 0);
        assertEquals(test_clothing.getType().compareTo("Type_Type"), 0);
        assertEquals(test_clothing.getDescription().compareTo("Description_Description"), 0);
        assertEquals(test_clothing.getUrlShop().compareTo("UrlShop_UrlShop"), 0);
        assertEquals(test_clothing.getUrlImage().compareTo("UrlImage_UrlImage"), 0);
        assertEquals(test_clothing.getOnSale().compareTo("OnSale_OnSale"), 0);
        assertEquals(test_clothing.getPrice().compareTo("Price_Price"), 0);
        assertEquals(test_clothing.getPartNumber().compareTo("PartNumber_PartNumber"), 0);
        assertEquals(test_clothing.getObjectID().compareTo("ObjectID_ObjectID"), 0);
        assertEquals(test_clothing.getBrand().compareTo("Brand_Brand"), 0);
        assertEquals(test_clothing.getName().compareTo("Name_Name"), 0);

    }
    public void getClothingTest(clothing[] AllClothes) {
        model = new Models();
        model.setArrayClothes(getClothing(AllClothes));
        assertThat(model.getArrayClothes().length==47);
        assertThat(model.getArrayLikedClothes().length == 10);


    }

    public void getClothingTest(clothing[] AllClothes) {
        model = new Models();
        model.setArrayClothes(getClothing(AllClothes));
        assertThat(model. model.getMyStringArrayFilters()=="Men"||model. model.getMyStringArrayFilters()=="Women");
    }

    public void getHtmlTest () {
        assertNull(WebServiceAdaptor.getHTML("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/%s/%s"));
    }

}