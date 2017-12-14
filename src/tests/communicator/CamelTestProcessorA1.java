package tests.communicator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class CamelTestProcessorA1 implements Processor {
  private CamelTester1 tester;
  
  public CamelTestProcessorA1(CamelTester1 tester) {
    this.tester = tester;
  }
  
  public void process(Exchange exchange) {
    this.tester.count += 100;
    exchange.getOut().setBody("Received");
  }
}
