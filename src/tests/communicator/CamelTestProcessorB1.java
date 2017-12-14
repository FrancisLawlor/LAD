package tests.communicator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CamelTestProcessorB1 implements Processor {
  private CamelTester1 tester;
  
  public CamelTestProcessorB1(CamelTester1 tester) {
    this.tester = tester;
  }
  
  public void process(Exchange exchange) {
    this.tester.count += 10;
    exchange.getOut().setBody("Received");
  }
}