package de.micromata.opengis.kml.v_2_2_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class CoordinatesConverter
        extends XmlAdapter<String, List<Coordinate>> {

    @Override
    public String marshal(final List<Coordinate> dt)
            throws Exception {
        final StringBuilder sb = new StringBuilder();
        dt.forEach((coord) -> {
            sb.append(coord).append(" ");
        }); // FIXME - this should use a collector
        return sb.toString().trim();
    }

    @Override
    public List<Coordinate> unmarshal(final String s)
            throws Exception {
        final String[] coords = s.replaceAll(",[\\s]+", ",").trim().split("\\s+");
        List<Coordinate> coordinates = new ArrayList<>();
        if (coords.length <= 0) {
            return coordinates;
        }
        for (String string : coords) {
            coordinates.add(new Coordinate(string));
        }
        return coordinates;
    }

}
