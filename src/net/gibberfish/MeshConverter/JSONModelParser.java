package net.gibberfish.MeshConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class JSONModelParser {
	
	static JSONParser parser;

	public static void parse(GibberModel gm, String sSrcFile) {
		try {
			parser = new JSONParser();

			File f = new File(sSrcFile);
			gm.filename = f.getName();
			Object obj = parser.parse(new FileReader(f));
	 
			JSONObject jsonObject = (JSONObject) obj;
			
			//vertices
			if (jsonObject.containsKey("verticeSize"))
				gm.verticeSize = Integer.valueOf(jsonObject.get("verticeSize").toString());
			if (jsonObject.containsKey("verticeItems"))
				gm.verticeItems = Integer.valueOf(jsonObject.get("verticeItems").toString());
			if (jsonObject.containsKey("vertices")){
				JSONArray verticesArray = (JSONArray) jsonObject.get("vertices");
				gm.vertices = new float[verticesArray.size()];
				int i=0;
				Iterator<Double> diterator = verticesArray.iterator();
				while (diterator.hasNext()) {
					gm.vertices[i]=diterator.next().floatValue();
					i++;
				}
			}
			
			//Indices
			if (jsonObject.containsKey("indiceSize"))
				gm.indiceSize = Integer.valueOf(jsonObject.get("indiceSize").toString());
			if (jsonObject.containsKey("indiceItems"))
				gm.indiceItems = Integer.valueOf(jsonObject.get("indiceItems").toString());
			if (jsonObject.containsKey("indices")){
				JSONArray indicesArray = (JSONArray) jsonObject.get("indices");
				gm.indices = new short[indicesArray.size()];
				int i=0;
				Iterator<Long> literator = indicesArray.iterator();
				while (literator.hasNext()) {
					Long l = literator.next().longValue();
					gm.indices[i]= l.shortValue();
					i++;
				}
			}
			
			//texture
			if (jsonObject.containsKey("texture")){
				gm.texture = (String) jsonObject.get("texture");
				if (jsonObject.containsKey("textureSize"))
					gm.textureSize = Integer.valueOf(jsonObject.get("textureSize").toString());
				if (jsonObject.containsKey("textureItems"))
					gm.textureItems = Integer.valueOf(jsonObject.get("textureItems").toString());
			}
			if (jsonObject.containsKey("textureCoordinates")){
				JSONArray texturesArray = (JSONArray) jsonObject.get("textureCoordinates");
				gm.textureCoords = new float[texturesArray.size()];
				int i=0;
				Iterator<Double> diterator = texturesArray.iterator();
				while (diterator.hasNext()) {
					gm.textureCoords[i]=diterator.next().floatValue();
					i++;
				}
			}
			
			//Colors
			if (jsonObject.containsKey("colorSize"))
				gm.colorSize = Integer.valueOf(jsonObject.get("colorSize").toString());
			if (jsonObject.containsKey("colorItems"))
				gm.colorItems = Integer.valueOf(jsonObject.get("colorItems").toString());
			if (jsonObject.containsKey("colors")){
				JSONArray colorsArray = (JSONArray) jsonObject.get("colors");
				gm.colors = new float[colorsArray.size()];
				int i=0;
				Iterator<Double> diterator = colorsArray.iterator();
				while (diterator.hasNext()) {
					gm.colors[i]=diterator.next().floatValue();
					i++;
				}
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
