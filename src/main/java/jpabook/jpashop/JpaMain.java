package jpabook.jpashop;

import jpabook.jpashop.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

            // 회원
            Member member = new Member();
            member.setCity("Seoul");
            member.setName("김차차");
            member.setStreet("서울 어딘가");
            member.setZipcode("12345");

            em.persist(member);

            // 카테고리
            Category category2 = new Category();
            category2.setName("노트북");

            Category category3 = new Category();
            category3.setName("태블릿");

            Category category1 = new Category();
            category1.setName("전자기기");
            category1.getChild().add(category2);
            category1.getChild().add(category3);

            category2.setParent(category1);
            category3.setParent(category1);

            // 아이템
            Item item1 = new Item();
            item1.setName("맥북");
            item1.setPrice(2000000);
            item1.setStockQuantity(10);
            item1.getCategories().add(category1);
            item1.getCategories().add(category2);

            Item item2 = new Item();
            item2.setName("아이패드");
            item2.setPrice(700000);
            item2.setStockQuantity(100);
            item1.getCategories().add(category1);
            item1.getCategories().add(category3);

            category1.getItems().add(item1);
            category1.getItems().add(item2);

            category2.getItems().add(item1);
            category3.getItems().add(item2);

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);

            em.persist(item1);
            em.persist(item2);

            // 주문 아이템
            OrderItem orderItem1 = new OrderItem();
            orderItem1.setItem(item1);
            orderItem1.setOrderPrice(item1.getPrice());
            orderItem1.setCount(1);

            OrderItem orderItem2 = new OrderItem();
            orderItem2.setItem(item2);
            orderItem2.setOrderPrice(item2.getPrice());
            orderItem2.setCount(1);

            // 배송
            Delivery delivery = new Delivery();
            delivery.setCity(member.getCity());
            delivery.setStreet(member.getStreet());
            delivery.setZipcode(member.getZipcode());
            delivery.setSatus(DeliveryStatus.PRODUCT_PREPARING);

            // 주문(배송과, 주문아이템 cascade)
            Order order = new Order();
            order.addDelivery(delivery);
            order.addOrderItem(orderItem1);
            order.addOrderItem(orderItem2);
            order.setStatus(OrderStatus.PAY_COMPLETED);
            order.setOrderDate(LocalDateTime.now());
            member.addOrder(order);

            em.persist(order);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
