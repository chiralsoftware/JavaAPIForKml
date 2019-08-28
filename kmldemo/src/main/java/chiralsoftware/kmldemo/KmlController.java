package chiralsoftware.kmldemo;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import java.io.StringWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class KmlController {

    @GetMapping("/sample.kml")
    public ResponseEntity<String> kml() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(createSample());
    }

    private static String createSample() {

        final Kml kml = new Kml();
        final Document doc = kml.createAndSetDocument().withName("KML Sample").withOpen(true);

        final Folder folder = doc.createAndAddFolder();
        folder.withName("The best country").withOpen(true);
        createPlacemarkWithChart(doc, folder, -103.5286299241638, 41.26035225962401, "North America", 17);
        
        final StringWriter writer = new StringWriter();
        kml.marshal(writer);
        return writer.toString();
    }

    private static void createPlacemarkWithChart(Document document, Folder folder, double longitude, double latitude,
            String continentName, int coveredLandmass) {

        int remainingLand = 100 - coveredLandmass;
        final Icon icon = new Icon()
                .withHref("http://chart.apis.google.com/chart?chs=380x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p&chf=bg,s,ffffff00");
        final Style style = document.createAndAddStyle();
        style.withId("style_" + continentName) // set the stylename to use this style from the placemark
                .createAndSetIconStyle().withScale(5.0).withIcon(icon); // set size and icon
        style.createAndSetLabelStyle().withColor("ff43b3ff").withScale(5.0); // set color and size of the continent name

        final Placemark placemark = folder.createAndAddPlacemark();
        // use the style for each continent
        placemark.withName(continentName)
                .withStyleUrl("#style_" + continentName)
                // 3D chart imgae
                .withDescription(
                        "<![CDATA[<img src=\"http://chart.apis.google.com/chart?chs=430x200&chd=t:" + coveredLandmass + "," + remainingLand + "&cht=p3&chl=" + continentName + "|remaining&chtt=Earth's surface\" />")
                // coordinates and distance (zoom level) of the viewer
                .createAndSetLookAt().withLongitude(longitude).withLatitude(latitude).withAltitude(0).withRange(12000000);

        placemark.createAndSetPoint().addToCoordinates(longitude, latitude); // set coordinates
    }
}
