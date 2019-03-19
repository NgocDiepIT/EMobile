package listener;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApplicationListener implements ServletContextListener  {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        GoogleCredentials credentials = null;
        try {
            String path = servletContextEvent.getServletContext().getRealPath("WEB-INF/MyCloudKey.json");
            InputStream serviceAccount = new FileInputStream(path);

            credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();
            ServletContext sc = servletContextEvent.getServletContext();
            sc.setAttribute("db", db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
    }
}
