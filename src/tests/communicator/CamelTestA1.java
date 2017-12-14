package tests.communicator;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelTestA1 {
  public static void main(String[] args) throws Exception {
    
    CamelContext context = new DefaultCamelContext();
    
    CamelTester1 tester = new CamelTester1();
    
    ProducerTemplate template = context.createProducerTemplate();
    
    Processor processor = new CamelTestProcessorA1(tester);
    
    context.addRoutes(new CamelTestRouteA1(processor));
    
    context.start();

    Thread.sleep(10000);
    template.sendBody(CamelTestRouteB1.INBOUND, "From A to B");
    
    int old = 0;
    while (true) {
      System.out.println(tester.count);
      Thread.sleep(1000);
      
      if (tester.count > old) {
        template.sendBody(CamelTestRouteB1.INBOUND, "From A to B");
        old = tester.count;
      }
    }
  }
}
