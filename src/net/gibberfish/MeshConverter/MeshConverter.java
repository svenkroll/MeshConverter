package net.gibberfish.MeshConverter;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.json.simple.JSONObject;

public class MeshConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String sSrcFile;
		String sDstPath;
		String sType;
		
		// create Options object
        Options options = new Options();
        options.addOption("i", true, "Input File");
        options.addOption("t", true, "Input Type (JSON or OBJ)");
        options.addOption("o", true, "Output Path");
        
        // parse the command line options
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            // something bad happened so output help message
            System.out.println("Error in parsing arguments:n" + e.getMessage());
        }

        
        if (!cmd.hasOption("i") || !cmd.hasOption("o") || !cmd.hasOption("t")){
            // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "java -jar ModelConverter.jar ", options );
        }else{
        	sSrcFile = cmd.getOptionValue("i");
        	sDstPath = cmd.getOptionValue("o");
        	sType = cmd.getOptionValue("t");
		
	        GibberModel model = new GibberModel();
	        
	        if (sType.equals("JSON"))
	        {
	        	model.loadJSON(sSrcFile);
	        }
	        if (sType.equals("OBJ"))
	        {
	        	model.loadOBJ(sSrcFile);
	        }
	        
        	model.generateOutBuffers();
        	
	        writeMetaJSON(sDstPath + "/" + model.getFilename() + ".gbjson", model);
	        writeDataBin(sDstPath + "/" + model.getFilename() + ".gbbin", model);

        }
	}
	
	private static void writeDataBin(String sDestFile, GibberModel m) {
		try 
        {
      	  FileOutputStream fos = new FileOutputStream(sDestFile); 
      	  DataOutputStream dos = new DataOutputStream(fos);
      	  
      	  if (m.hasVertices()){
	      	  for (int i=0; i < m.getOutVertices().length; i++){
	      		  dos.writeByte( m.getOutVertices()[i]);	
	      	  }
      	  }
      	  if (m.hasIndices()){
      		  for (int i=0; i < m.getOutIndices().length; i++){
        		  dos.writeByte( m.getOutIndices()[i]);	
        	  }
      	  }
      	  if (m.hasTexture()){
      		for (int i=0; i < m.getOutTextureCoords().length; i++){
	      		  dos.writeByte( m.getOutTextureCoords()[i]);	
	      	  }
      	  }
      	  if (m.hasColors()){
	      	  for (int i=0; i < m.getOutColors().length; i++){
	      		  dos.writeByte( m.getOutColors()[i]);	
	      	  }
      	  }
          fos.close();   

        }     
        catch(FileNotFoundException ex){
        	System.out.println("FileNotFoundException : " + ex);  
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
		}
	}

	private static void writeMetaJSON(String sDestFile, GibberModel m){
		
		JSONObject obj = new JSONObject();
		int Offset = 0;
		
		if (m.hasVertices()){
			obj.put("verticeSize", m.getVerticeSize());
			obj.put("verticeItems", m.getVerticeItems());
			obj.put("verticeByteOffset", Offset);
			obj.put("verticeByteLength", m.getVerticesByteLength());
			Offset += (m.getVerticesByteLength() * m.getVerticeItems() * m.getVerticeSize());
		}
		if (m.hasIndices()){
			obj.put("indiceSize", m.getIndiceSize());
			obj.put("indiceItems", m.getIndiceItems());
			obj.put("indiceByteOffset", Offset);
			obj.put("indiceByteLength", m.getIndicesByteLength());
			Offset += (m.getIndicesByteLength() * m.getIndiceItems() * m.getIndiceSize());
		}
		if (m.hasTexture()){
			obj.put("textureSize", m.getTextureSize());
			obj.put("textureItems", m.getTextureItems());
			obj.put("texture", m.getTexture());
			obj.put("textureByteOffset", Offset);
			obj.put("textureByteLength", m.getTextureCoordByteLength());
			Offset += (m.getTextureCoordByteLength() * m.getTextureItems() * m.getTextureSize());
		}
		if (m.hasColors()){
			obj.put("colorSize", m.getColorSize());
			obj.put("colorItems", m.getColorItems());
			obj.put("colorByteOffset", Offset);
			obj.put("colorByteLength", m.getColorsByteLength());
			Offset += (m.getColorsByteLength() * m.getColorItems() * m.getColorSize());
		}
		try {
	 
			FileWriter file = new FileWriter(sDestFile);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
		System.out.print("MetaJSON ready: " + obj);
	}
}
