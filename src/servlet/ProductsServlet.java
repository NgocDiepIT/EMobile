package servlet;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import model.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //lấy DL từ CSDL
        Firestore db = (Firestore) getServletContext().getAttribute("db");

        ApiFuture<QuerySnapshot> query = db.collection("products").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();

            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            List<Product> products = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {
                Product product = new Product();
                product.id = document.getString("id");
                product.name = document.getString("name");
                product.price = document.getLong("price");
                product.description = document.getString("description");

                products.add(product);

                System.out.println("ID: " + document.getId());
                System.out.println("Name: " + document.getString("name"));
                System.out.println("Price: " + document.getLong("price"));
                System.out.println("Description: " + document.getString("description"));
            }

            req.setAttribute("products",products);
            RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher("/products.jsp");
            requestDispatcher.forward(req, resp);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
