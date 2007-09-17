//
// EditImageName.java
//

import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.ome.OMEXMLMetadata;

/** Edits the given file's image name (but does not save back to disk). */
public class EditImageName {

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: java EditImageName file");
      return;
    }
    ImageReader reader = new ImageReader();
    // record metadata to OME-XML format
    reader.setMetadataStore(new OMEXMLMetadata());
    String id = args[0];
    System.out.print("Reading metadata ");
    reader.setId(id); 
    OMEXMLMetadata omexmlMeta = (OMEXMLMetadata) reader.getMetadataStore();
    System.out.println(" [done]");

    // get image name
    Integer zero = new Integer(0);
    String name = omexmlMeta.getImageName(zero);
    System.out.println("Initial Image name = " + name);
    // change image name (reverse it)
    char[] arr = name.toCharArray();
    for (int i=0; i<arr.length/2; i++) {
      int i2 = arr.length - i - 1;
      char c = arr[i];
      char c2 = arr[i2];
      arr[i] = c2;
      arr[i2] = c;
    }
    name = new String(arr);
    // save altered name back to OME-XML structure
    omexmlMeta.setImage(name, null, null, zero);
    System.out.println("Updated Image name = " + name);
    // output full OME-XML block
    System.out.println("Full OME-XML dump:");
    System.out.println(MetadataTools.getOMEXML(omexmlMeta));
  }

}
