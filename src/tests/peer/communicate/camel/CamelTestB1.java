package tests.peer.communicate.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelTestB1 {
  public static void main(String[] args) throws Exception {
    
    CamelContext context = new DefaultCamelContext();
    
    CamelTester1 tester = new CamelTester1();
    
    ProducerTemplate template = context.createProducerTemplate();
    
    Processor processor = new CamelTestProcessorB1(tester);
    
    context.addRoutes(new CamelTestRouteB1(processor));
    
    context.start();
    
    int old = 0;
    while (true) {
      System.out.println(tester.count);
      Thread.sleep(1000);

      if (tester.count > old) {
        template.sendBody(CamelTestRouteA1.INBOUND, "From B to A");
        old = tester.count;
      }
    }
  }
}
