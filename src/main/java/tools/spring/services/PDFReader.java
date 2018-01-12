package tools.spring.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PDFReader {

	private final Logger log = LogManager.getLogger(PDFReader.class);
	private final String fs = File.separator;
	private String docText = "";
	
	public void parseCatalogGeneratedText() {
		String catalogText = fileToString(System.getProperty("user.dir") + fs + "refinedCourseDescriptions.txt");
		try {
			antlrParser(catalogText);
		} catch (IOException e) {
			log.error("Error paring catalog", e);
		}
	}

	private String fileToString(String file) {
		log.info("Start read Catalog");
		try (Stream<String> stream = Files.lines(Paths.get(file))) {
			stream.collect(Collectors.toList()).forEach(line -> docText += line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("End read Catalog");
		return docText;
	}

//	public void readCatalogPDF() {
//		BodyContentHandler handler = new BodyContentHandler(-1);
//		Metadata metadata = new Metadata();
//		readPDF(handler, metadata, System.getProperty("user.dir") + "src"+fs+"main"+fs+"resources"+fs+"SIUE_Undergraduate_Catalog.pdf");
//		writeCatalog(handler);
//		outputMetaDataOfPDF(metadata);
//	}
	
//	private void readPDF(BodyContentHandler handler, Metadata metadata, String file) {
//		try {
//			FileInputStream inputstream = new FileInputStream(new File(file));
//			ParseContext pcontext = new ParseContext();
//			// parsing the document using PDF parser
//			Parser pdfparser = new PDFParser();
//			pdfparser.parse(inputstream, handler, metadata, pcontext);
//		} catch (IOException | SAXException | TikaException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
	
//	private void writeCatalog(BodyContentHandler handler) {
//		// getting the content of the document
//		System.out.println("Contents of the PDF to File");
//		// Use try-with-resource to get auto-closeable writer instance
//		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("catalog.txt"))) {
//			docText = handler.toString();
//			writer.write(handler.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}		
//	}
	
//	private void outputMetaDataOfPDF(Metadata metadata) {
//		// getting metadata of the document
//		System.out.println("Metadata of the PDF:");
//		String[] metadataNames = metadata.names();
//
//		for (String name : metadataNames) {
//			System.out.println(name + " : " + metadata.get(name));
//		}	
//	}
	
	private void antlrParser(String catalogText) throws IOException{
//		ANTLRInputStream input = new ANTLRInputStream(catalogText);
//        CatalogLexer lexer = new CatalogLexer(input);
//        lexer.removeErrorListeners();
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        CatalogParser parser = new CatalogParser(tokens);
//		parser.prog();
		
//        CatalogParser.FileContext fileContext = parser.file();                
//        CatalogVisitor visitor = new CatalogVisitor();                
//        visitor.visit(fileContext);  
		
//		ParseTreeWalker walker = new ParseTreeWalker();
//		AntlrCatalogListener listener = new AntlrCatalogListener();
//		walker.walk(listener, catalogSentenceContext);
		//System.out.println(tokens.getText());
		
		
		//    // Specify our entry point
//	    DrinkSentenceContext drinkSentenceContext = parser.drinkSentence();
//	    
//	    // Walk it and attach our listener
//	    ParseTreeWalker walker = new ParseTreeWalker();
//	    AntlrDrinkListener listener = new AntlrDrinkListener();
//	    walker.walk(listener, drinkSentenceContext);
	}

}
