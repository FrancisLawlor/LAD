package tests.camel;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class CamelTestRouteA1 extends RouteBuilder {
  private Processor processor;
  public static final String INBOUND =
      "restlet:http://localhost:10001/CamelTestRequest?restletMethod=post";
  
  public CamelTestRouteA1(Processor processor) {
    this.processor = processor;
  }
  
  @Override
  public void configure() {
    from(INBOUND).process(this.processor);
  }
}
