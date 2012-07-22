package net.gibberfish.MeshConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class OBJModelParser {

    private  Stack<float[]> vData;  	// List of Vertex Coordinates
    private  Stack<float[]> vtData;	 	// List of Texture Coordinates
    private  Stack<float[]> vnData; 	// List of Normal Coordinates
    private  Stack<short[]> fv; 			// Face Vertex Indices
    private  Stack<short[]> ft; 			// Face Texture Indices
    private  Stack<short[]> fn; 			// Face Normal Indices
    private  String FaceFormat; 		// Format of the Faces Triangles or Quads
    private  int FaceMultiplier; 		// Number of possible coordinates per face
    private  int PolyCount = 0; 		// The Models Polygon Count
    
    public OBJModelParser(){
    	vData = new Stack<float[]>();
    	vtData = new Stack<float[]>();
    	vnData = new Stack<float[]>();
    	fv = new Stack<short[]>();
    	ft = new Stack<short[]>();
    	fn = new Stack<short[]>();
    }
    
    public  void parse(GibberModel gm, String sSrcFile) {
        try {
            // Open a file handle and read the models data
        	File f = new File(sSrcFile);
			gm.filename = f.getName();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String  line = null;
            while((line = br.readLine()) != null) {
                if (line.startsWith("#")) { // Read Any Descriptor Data in the File
                    //System.out.println("Descriptor: "+line); //Uncomment to print out file descriptor data
                } else if (line.equals("")) {
                    //Ignore whitespace data
                } else if (line.startsWith("v ")) { // Read in Vertex Data
                    vData.add(ProcessData(line));
                } else if (line.startsWith("vt ")) { // Read Texture Coordinates
                    vtData.add(ProcessData(line));
                } else if (line.startsWith("vn ")) { // Read Normal Coordinates
                    vnData.add(ProcessData(line));
                } else if (line.startsWith("f ")) { // Read Face Data
                    ProcessfData(line);
                } else if (line.startsWith("mtllib ")) { // Read MAterial Library
                    ProcessMtlData(line);
                } else {
                	//ignore all other commands, for now
                }
            }
            br.close();
        } catch(IOException e) {
            System.out.println("Failed to find or read OBJ");
            System.err.println(e);
        }
        SetFaceRenderType();
        MergeArrays(gm);
    }
    
    private void ProcessMtlData(String line) {
		// TODO Auto-generated method stub
		
	}

	private void MergeArrays(GibberModel gm) {
    	gm.verticeSize = 3;//three floats per vertex to describe position
    	gm.verticeItems = vData.size();//Vertex count
    	gm.vertices = new float[vData.size() * 3];//Vertex float array
    	int i = 0;
    	for (float[] fVertex : vData){
    		for (float vCoord : fVertex){
    			gm.vertices[i] = vCoord;
    			i++;
    		}
    	}
    	gm.indiceSize = 1;//Always one
    	gm.indiceItems = fv.size()*3;//How much vertex to draw
    	gm.indices = new short[fv.size()*3];//Indices float array
    	int j = 0;
    	for (short[] iIndice : fv){
    		for (short indice : iIndice){
    			gm.indices[j] = (short) (indice - 1 );
    			j++;
    		}
    	}
    	
    	gm.texture = "barell.jpg";			//texture file path
    	gm.textureSize = 2;				//float coords per vertex, always 2 for 2d tex
    	gm.textureItems = vData.size();	//texture coords count, always the same as verticeItems
    	gm.textureCoords = new float[vData.size() * 2];	// Texture Coordinates Float Array, every vertex needs two floats to describe position on 2d texture
    	for (int k=0; k<fv.size();k++){
    		short[] sFaceTextureIndice = ft.get(k);
    		short[] sFaceVertexIndice = fv.get(k);
    		for (int l=0; l<3; l++){
    			gm.textureCoords[sFaceVertexIndice[l]*2-2] = vtData.get(sFaceTextureIndice[l]-1)[0];
    			gm.textureCoords[sFaceVertexIndice[l]*2-1] = vtData.get(sFaceTextureIndice[l]-1)[1];
    		}
    	}
    	
    	gm.colorSize = 0;	//floats per color item
    	gm.colorItems = 0;	//count color items, always the same as verticeItems
    	gm.colors = null;
	}

	private  float[] ProcessData(String read) {
        final String s[] = read.split("\\s+");
        return (ProcessFloatData(s)); //returns an array of processed float data
    }
    
    private  float[] ProcessFloatData(String sdata[]) {
        float data[] = new float[sdata.length-1];
        for (int loop=0; loop < data.length; loop++) {
            data[loop] = Float.parseFloat(sdata[loop+1]);
        }
        return data; // return an array of floats
    }
    
    private  void ProcessfData(String fread) {
        PolyCount++;
        String s[] = fread.split("\\s+");
        if (fread.contains("//")) { // Pattern is present if obj has only v and vn in face data
            for (int loop=1; loop < s.length; loop++) {
                s[loop] = s[loop].replaceAll("//","/0/"); //insert a zero for missing vt data
            }
        }
        ProcessfIntData(s); // Pass in face data
    }
    
    private  void ProcessfIntData(String sdata[]) {
        short vdata[] = new short[sdata.length-1];
        short vtdata[] = new short[sdata.length-1];
        short vndata[] = new short[sdata.length-1];
        for (int loop = 1; loop < sdata.length; loop++) {
            String s = sdata[loop];
            String[] temp = s.split("/");
            vdata[loop-1] = Short.valueOf(temp[0]); //always add vertex indices
            if (temp.length > 1) { // we have v and vt data
                vtdata[loop-1] = Short.valueOf(temp[1]); // add in vt indices
            } else {
                vtdata[loop-1] = 0; // if no vt data is present fill in zeros
            }
            if (temp.length > 2) { // we have v, vt, and vn data
                vndata[loop-1] = Short.valueOf(temp[2]); // add in vn indices
            } else {
                vndata[loop-1] = 0; //if no vn data is present fill in zeros
            }
        }
        fv.add(vdata);
        ft.add(vtdata);
        fn.add(vndata);
    }
    
    private  void SetFaceRenderType() {
        final short[] temp = (short[]) fv.get(0);
        if ( temp.length == 3) { 
            FaceFormat = "GL_TRIANGLES"; // The faces come in sets of 3 so we have triangular faces
            FaceMultiplier = 3;
        } else if (temp.length == 4) {
            FaceFormat = "GL_QUADS"; // The faces come in sets of 4 so we have quadrilateral faces
            FaceMultiplier = 4;
        } else {
            FaceFormat = "GL_POLYGON"; // Fall back to render as free form polygons
        }
    }
}
