package org.supercsv.webtests;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class PartialReadWriteExamplesTest {
public static class Order {
Integer orderNumber;
Integer parentOrder;
Integer productNumber;
String userComment;

public Integer getOrderNumber() {
	return orderNumber;
}

public Integer getParentOrder() {
	return parentOrder;
}

public Integer getProductNumber() {
	return productNumber;
}

public String getUserComment() {
	return userComment;
}

public void setOrderNumber(final int orderNumber) {
	this.orderNumber = orderNumber;
}

public void setParentOrder(final int parentOrder) {
	this.parentOrder = parentOrder;
}

public void setProductNumber(final int productNumber) {
	this.productNumber = productNumber;
}

public void setUserComment(final String userComment) {
	this.userComment = userComment;
}
}

@Test
public void should_partial_read() throws Exception {
	// content of a file containing orders and product numbers
	final String fileData = "orderNumber, productNumber\n1,22";
	
	// setup conversion from String to integers (as our fields are type int)
	final CellProcessor[] processing = new CellProcessor[] { new ParseInt(), new ParseInt() };
	
	// the actual reading
	final CsvBeanReader reader = new CsvBeanReader(new StringReader(fileData), CsvPreference.EXCEL_PREFERENCE);
	// get header to identify what fields to populate
	final String[] header = reader.getCSVHeader(true);
	final Order order = reader.read(PartialReadWriteExamplesTest.Order.class, header, processing);
	
	// show that only part of the object has been populated with values
	System.out.print("order: " + order.getOrderNumber() //
		+ " product: " + order.getProductNumber() //
		+ " parent: " + order.getParentOrder() + "\n**\n");
	
}

@Test
public void should_partial_write() throws Exception {
	// The data to write
	final Order mainOrder = new Order();
	mainOrder.setOrderNumber(1);
	mainOrder.setProductNumber(42);
	mainOrder.setUserComment("some comment");
	final Order subOorder = new Order();
	subOorder.setOrderNumber(2);
	subOorder.setParentOrder(1);
	subOorder.setProductNumber(43);
	
	// for testing write to a string rather than a file
	final StringWriter outFile = new StringWriter();
	
	// setup header for the file and processors. Notice the match between the header and the attributes of the
	// objects to write. The rules are that
	// - if optional "parent orders" are absent, write -1
	// - and optional user comments absent are written as ""
	final String[] header = new String[] { "orderNumber", "parentOrder", "productNumber", "userComment" };
	final CellProcessor[] Processing = new CellProcessor[] { null, new ConvertNullTo(-1), null,
		new ConvertNullTo("\"\"") };
	
	// write the partial data
	final CsvBeanWriter writer = new CsvBeanWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
	writer.writeHeader(header);
	writer.write(mainOrder, header, Processing);
	writer.write(subOorder, header, Processing);
	writer.close();
	
	// show output
	System.out.println(outFile.toString());
}
}
