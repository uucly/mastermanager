package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;






import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.openlayers.api.Bounds;
import org.wicketstuff.openlayers.api.Control;
import org.wicketstuff.openlayers.api.LonLat;
import org.wicketstuff.openlayers.api.SphericalMercatorLonLat;
import org.wicketstuff.openlayers.api.layer.GMap;
import org.wicketstuff.openlayers.api.layer.OSM;
import org.wicketstuff.openlayers.api.layer.OSM.OSMLayer;
import org.wicketstuff.openlayers.OpenLayersMap;
import org.wicketstuff.openlayers.api.layer.Layer;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;

public class OpenStreetMapPage extends BasePage{

	private static final long serialVersionUID = 1L;

	private static final String GMAPS_KEY = "ABQIAAAA97_buYctDhaanPL-uED8txTwM0brOpm-All5BF6PoaKBxRWWERTl_Z3abREy_5Ldy_yMuCsn5M0FmQ";

	public OpenStreetMapPage() {
		
		List<Layer> layers = new ArrayList<Layer>();
		HashMap<String, String> optionsLayer = new HashMap<String, String>();
		// optionsLayer.put("type", "G_HYBRID_MAP");
		Layer layer = new GMap("GMap", GMAPS_KEY, "2", optionsLayer);
		layers.add(layer);
		HashMap<String, String> mapOptions = new HashMap<String, String>();
		Bounds boundsExtend = new Bounds(new LonLat(-20037508.34,-20037508.34), new LonLat(20037508.34,20037508.34));
		mapOptions.put("maxExtent", boundsExtend.getJSconstructor());
		OpenLayersMap map = new OpenLayersMap("map", true, layers, mapOptions);
		map.setCenter(new SphericalMercatorLonLat(10.2, 48.9), 13);
		add(map);
		/*getApplication().getMarkupSettings().setStripWicketTags(true);
		setOutputMarkupId(true);
		List<Layer> layers = new ArrayList<Layer>();
		
		
		Layer layerOSMTilesAtHome = new OSM("Osmarender", OSMLayer.TilesAtHome);
		Layer layerOSMMapnik = new OSM("Mapnik", OSMLayer.Mapnik);
		Layer layerOSMCycleMap = new OSM("CycleMap", OSMLayer.CycleMap);
		layers.add(layerOSMMapnik);
		layers.add(layerOSMTilesAtHome);
		layers.add(layerOSMCycleMap);

		HashMap<String, String> mapOptions = new HashMap<String, String>();
		Bounds boundsExtend = new Bounds(
				new LonLat(-20037508.34, -20037508.34), new LonLat(20037508.34,
						20037508.34));
		mapOptions.put("maxExtent", boundsExtend.getJSconstructor());
		mapOptions
				.put("projection", "new OpenLayers.Projection('EPSG:900913')");
		mapOptions.put("displayProjection",
				"new OpenLayers.Projection('EPSG:4326')");
		mapOptions.put("units", "'meters'");
		mapOptions.put("maxResolution", "156543");
		mapOptions.put("numZoomLevels", "18");

		OpenLayersMap map = new OpenLayersMap("map", true, layers, mapOptions);
		map.addControl(Control.LayerSwitcher);
		map.addControl(Control.MousePosition);
		map.addControl(Control.KeyboardDefaults);

		// map.setCenter(new LonLat(10.2, 48.9));
		// map.setZoom(3);

		add(map);*/
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.MAP;
	}

}
