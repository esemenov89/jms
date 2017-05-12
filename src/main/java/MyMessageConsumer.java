import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.Timestamp;

/**
 * Created by admin on 12.05.2017.
 */
public class MyMessageConsumer implements Runnable, ExceptionListener {
    public void run() {
        try {

            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive(1);

            long l = System.currentTimeMillis();
            // Wait for a message
            while(true) {
                message = consumer.receive(1000);
                if (message instanceof TextMessage ) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    if(text!=null)
                        System.out.println("Received: " + text);
                } else {
                    if(message!=null)
                        System.out.println("Received: " + message);
                }
                Thread.sleep(100);
                if(System.currentTimeMillis()-l>60000){
                    break;
                }
            }

            //consumer.close();
           // session.close();
            //connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
        finally {
            //consumer.close();
            // session.close();
            //connection.close();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}