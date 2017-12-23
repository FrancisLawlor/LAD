package tests.peer.communicate.camel;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class CamelTestRouteB1 extends RouteBuilder {
  private Processor processor;
  public static final String INBOUND =
      "restlet:http://localhost:10002/CamelTestRequest?restletMethod=post";
  
  public CamelTestRouteB1(Processor processor) {
    this.processor = processor;
  }
  
  @Override
  public void configure() {
    from(INBOUND).process(this.processor);
  }
}