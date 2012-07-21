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

public class GibberModel {
	
	JSONParser parser;
	
	float[] vertices;
	float[] textureCoords;
	float[] colors;
    short[] indices;
    
    int verticeSize;
	int verticeItems;
	int indiceSize;
	int indiceItems;
    String texture;
    int textureSize;
    int textureItems;
    int colorSize;;
    int colorItems;
    
    byte[] outVertices;
    byte[] outColors;
    byte[] outIndices;
    byte[] outTextureCoords;
    
    int verticesByteLength;
    int indicesByteLength;
    int colorsByteLength;
    int textureCoordByteLength;
    
    String filename;
  
    /**
     * Constructor
     */
	public GibberModel() {
		parser = new JSONParser();
	    verticesByteLength = 4;
	    indicesByteLength = 2;
	    colorsByteLength = 4;
	    textureCoordByteLength = 4;
	}
	
	/**
	 * Convert all float/short[] into byte[]
	 */
	public void generateOutBuffers() {
		
		//convert vertices from float to 4byte binary
		if (vertices.length > 0){
			int j=0;
		    outVertices=new byte[(vertices.length*verticesByteLength)];
		    
		    for (int i=0;i < vertices.length;i++) {
		      int data=Float.floatToIntBits(vertices[i]);
		      outVertices[j++]=(byte)(data>>>0);
		      outVertices[j++]=(byte)(data>>>8);
		      outVertices[j++]=(byte)(data>>>16);
		      outVertices[j++]=(byte)(data>>>24);
		    }
		}
	  //convert indices from short to 2byte binary
		if (indices.length > 0){
	  		int k=0;
	  	    outIndices=new byte[(indices.length*indicesByteLength)];
	  	    
	  	    for (int i=0;i < indices.length;i++) {
	  	    	short data = indices[i];
	  	    	outIndices[k++]=(byte)(data>>>0);
	  	    	outIndices[k++]=(byte)(data>>>8);
	  	    }
		}
	  //convert colors from float to 4byte binary
		if (colors.length > 0){
	  	  int l=0;
		    outColors=new byte[(colors.length*colorsByteLength)];
		    
		    for (int i=0;i < colors.length;i++) {
		      int data=Float.floatToIntBits(colors[i]);
		      outColors[l++]=(byte)(data>>>0);
		      outColors[l++]=(byte)(data>>>8);
		      outColors[l++]=(byte)(data>>>16);
		      outColors[l++]=(byte)(data>>>24);
		    }
		}
		//convert textureCoords from float to 4byte binary
		if (textureCoords.length > 0){
	  	  int l=0;
	  	  outTextureCoords=new byte[(textureCoords.length*textureCoordByteLength)];
		    
		    for (int i=0;i < textureCoords.length;i++) {
		      int data=Float.floatToIntBits(textureCoords[i]);
		      outTextureCoords[l++]=(byte)(data>>>0);
		      outTextureCoords[l++]=(byte)(data>>>8);
		      outTextureCoords[l++]=(byte)(data>>>16);
		      outTextureCoords[l++]=(byte)(data>>>24);
		    }
		}
	}


	public void loadOBJ(String sSrcFile){
		
	}
	
	public void loadJSON(String sSrcFile) {
		try {
			
			File f = new File(sSrcFile);
			filename = f.getName();
			Object obj = parser.parse(new FileReader(f));
	 
			JSONObject jsonObject = (JSONObject) obj;
			
			//vertices
			if (jsonObject.containsKey("verticeSize"))
				verticeSize = Integer.valueOf(jsonObject.get("verticeSize").toString());
			if (jsonObject.containsKey("verticeItems"))
				verticeItems = Integer.valueOf(jsonObject.get("verticeItems").toString());
			if (jsonObject.containsKey("vertices")){
				JSONArray verticesArray = (JSONArray) jsonObject.get("vertices");
				vertices = new float[verticesArray.size()];
				int i=0;
				Iterator<Double> diterator = verticesArray.iterator();
				while (diterator.hasNext()) {
					vertices[i]=diterator.next().floatValue();
					i++;
				}
			}
			
			//Indices
			if (jsonObject.containsKey("indiceSize"))
				indiceSize = Integer.valueOf(jsonObject.get("indiceSize").toString());
			if (jsonObject.containsKey("indiceItems"))
				indiceItems = Integer.valueOf(jsonObject.get("indiceItems").toString());
			if (jsonObject.containsKey("indices")){
				JSONArray indicesArray = (JSONArray) jsonObject.get("indices");
				indices = new short[indicesArray.size()];
				int i=0;
				Iterator<Long> literator = indicesArray.iterator();
				while (literator.hasNext()) {
					Long l = literator.next().longValue();
					indices[i]= l.shortValue();
					i++;
				}
			}
			
			//texture
			if (jsonObject.containsKey("texture")){
				texture = (String) jsonObject.get("texture");
				if (jsonObject.containsKey("textureSize"))
					textureSize = Integer.valueOf(jsonObject.get("textureSize").toString());
				if (jsonObject.containsKey("textureItems"))
					textureItems = Integer.valueOf(jsonObject.get("textureItems").toString());
			}
			if (jsonObject.containsKey("textureCoordinates")){
				JSONArray texturesArray = (JSONArray) jsonObject.get("textureCoordinates");
				textureCoords = new float[texturesArray.size()];
				int i=0;
				Iterator<Double> diterator = texturesArray.iterator();
				while (diterator.hasNext()) {
					textureCoords[i]=diterator.next().floatValue();
					i++;
				}
			}
			
			//Colors
			if (jsonObject.containsKey("colorSize"))
				colorSize = Integer.valueOf(jsonObject.get("colorSize").toString());
			if (jsonObject.containsKey("colorItems"))
				colorItems = Integer.valueOf(jsonObject.get("colorItems").toString());
			if (jsonObject.containsKey("colors")){
				JSONArray colorsArray = (JSONArray) jsonObject.get("colors");
				colors = new float[colorsArray.size()];
				int i=0;
				Iterator<Double> diterator = colorsArray.iterator();
				while (diterator.hasNext()) {
					colors[i]=diterator.next().floatValue();
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
	
	public byte[] getOutVertices() {
		return outVertices;
	}

	public byte[] getOutIndices() {
		return outIndices;
	}

	public byte[] getOutColors() {
		return outColors;
	}
	
	public byte[] getOutTextureCoords() {
		return outTextureCoords;
	}
	
	public String getFilename() {
		return filename;
	}

	public int getVerticeSize() {
		return verticeSize;
	}

	public int getVerticeItems() {
		return verticeItems;
	}

	public int getIndiceSize() {
		return indiceSize;
	}

	public int getIndiceItems() {
		return indiceItems;
	}

	public String getTexture() {
		return texture;
	}

	public int getTextureSize() {
		return textureSize;
	}

	public int getTextureItems() {
		return textureItems;
	}

	public int getColorSize() {
		return colorSize;
	}

	public int getColorItems() {
		return colorItems;
	}
	
	public boolean hasTexture(){
		return textureCoords.length > 0 ? true : false;
	}
	
	public boolean hasVertices(){
		return vertices.length > 0 ? true : false;
	}
	
	public boolean hasIndices(){
		return indices.length > 0 ? true : false;
	}
	
	public boolean hasColors(){
		return colors.length > 0 ? true : false;
	}
	
	public int getVerticesByteLength(){
		return verticesByteLength;
	}
	
	public int getIndicesByteLength(){
		return indicesByteLength;
	}
	
	public int getTextureCoordByteLength(){
		return textureCoordByteLength;
	}
	
	public int getColorsByteLength(){
		return colorsByteLength;
	}
}
