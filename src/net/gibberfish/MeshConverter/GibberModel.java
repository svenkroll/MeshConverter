package net.gibberfish.MeshConverter;

public class GibberModel {

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
		if (colors != null && colors.length > 0){
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
		OBJModelParser om = new OBJModelParser();
		om.parse(this, sSrcFile);
	}
	
	public void loadJSON(String sSrcFile) {
			JSONModelParser.parse(this, sSrcFile);
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
		return (textureCoords != null && textureCoords.length > 0) ? true : false;
	}
	
	public boolean hasVertices(){
		return (vertices != null && vertices.length > 0) ? true : false;
	}
	
	public boolean hasIndices(){
		return (indices != null && indices.length > 0) ? true : false;
	}
	
	public boolean hasColors(){
		
		return (colors != null && colors.length > 0) ? true : false;
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
