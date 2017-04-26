package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class Main {

    private SessionFactory sessionFactory;

    public static void main(String[] args) {
        Main main = new Main();
        main.doit();
    }

    private void doit() {
        createSession();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //This chunk is the part that changes...
        List result = session.createQuery( "select s from Student s" ).list();
        for ( Student student : (List<Student>) result ) {
            System.out.println(student);
        }

        //More queries here:
        List<Student> students = session.createQuery(
                "select s " +
                        "from Student s " +
                        "where s.name like :name" )
                .setParameter( "name", "T%" )
                .list();
        System.out.println(students);

        session.getTransaction().commit();
        session.close();
        endSession();
    }

    private void createSession() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("mydb.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("#ERROR " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private void endSession() {
        sessionFactory.close();
    }
}
