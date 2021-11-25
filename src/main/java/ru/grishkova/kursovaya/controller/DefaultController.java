package ru.grishkova.kursovaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.grishkova.kursovaya.entity.Order;
import ru.grishkova.kursovaya.entity.Basket;
import ru.grishkova.kursovaya.entity.Product;
import ru.grishkova.kursovaya.entity.ProductType;
import ru.grishkova.kursovaya.repository.BasketRepository;
import ru.grishkova.kursovaya.repository.OrderRepository;
import ru.grishkova.kursovaya.repository.ProductRepository;
import ru.grishkova.kursovaya.repository.ProductTypeRepository;

import java.util.*;

@Controller
public class DefaultController {

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BasketRepository basketRepository;

    @GetMapping("/")
    public String index(Model model) {
        Iterable<ProductType> types = productTypeRepository.findAll();
        Map<ProductType, List<Product>> map = new HashMap<>();
        types.forEach(type -> map.put(type, productRepository.findByProductType(type)));
        Iterable<Product> products = productRepository.findAll();
        List<Product> newProducts = new ArrayList<>();
        List<Product> hotProducts = new ArrayList<>();
        List<Product> saleProducts = new ArrayList<>();
        products.forEach(product -> {
            if(product.getHotProduct())
                hotProducts.add(product);
            if(product.getNewProduct())
                newProducts.add(product);
            if(product.getOldPrice()!=0)
                saleProducts.add(product);
        });
        model.addAttribute("newProducts", newProducts);
        model.addAttribute("hotProducts", hotProducts);
        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("map", map);
        return "index";
    }

//    @GetMapping("/product/{productId}")
//    public String product(@PathVariable("productId") long id, Model model) {
//        Product product = productRepository.findById(id).orElse(null);
//        model.addAttribute("product", product);
//        return "product";
//    }

    @GetMapping("/product")
    public String product(@RequestParam("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        Basket basket = new Basket();
        if(basketRepository.findByProduct(product) == null) {
            basket.setQuantity((long) 1);
            basket.setProduct(product);
            model.addAttribute("inBasket", false);
        }
        else {
            basket = basketRepository.findByProduct(product);
            model.addAttribute("inBasket", true);
        }
        model.addAttribute("basket", basket);
        model.addAttribute("types", productTypeRepository.findAll());
        return "product";
    }

    @PostMapping("product")
    public String basketAdd(@ModelAttribute Basket saveBasket, Model model){
        basketRepository.save(saveBasket);
        return basketList(model);
    }

    @GetMapping("/basket")
    public String basketList(Model model){
        Map<Basket, Double> map = new HashMap<>();
        Order order = new Order();
        Iterable<Basket> baskets = basketRepository.findAll();
        baskets.forEach(basket -> map.put(basket, basket.getQuantity() * basket.getProduct().getPrice()));
        model.addAttribute("order", order);
        model.addAttribute("map", map);
        model.addAttribute("types", productTypeRepository.findAll());
        return "basket";
    }

    @PostMapping("/basket")
    public String basketNull(@ModelAttribute Order order, Model model){
        String description = "";
        Iterable<Basket> baskets = basketRepository.findAll();
        order.setGood("");
        baskets.forEach(basket -> order.addGood(basket.getQuantity() + " " + basket.getProduct().getName() + "; "));
        orderRepository.save(order);
        basketRepository.deleteAll();
        return index(model);
    }

    @GetMapping("/productList")
    public String productList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "productList";
    }

    @GetMapping("productList/add")
    public String productListAdd(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        Iterable<ProductType> types = productTypeRepository.findAll();
        model.addAttribute("types", types);
        return "productForm";
    }

    @PostMapping("productList/add")
    public String productListAddSubmit(@ModelAttribute Product product, Model model){
        productRepository.save(product);
        return productList(model);
    }

    @GetMapping("/productList/delete/{productId}")
    public String productListDelete(@PathVariable("productId") long id, Model model) {
        productRepository.deleteById(id);
        return productList(model);
    }

    @GetMapping("/productList/edit/{productId}")
    public String productListEdit(@PathVariable("productId") long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        Iterable<ProductType> types = productTypeRepository.findAll();
        model.addAttribute("types", types);
        return "productForm";
    }

    @GetMapping("/productTypeList")
    public String productTypeList(Model model) {
        Iterable<ProductType> types = productTypeRepository.findAll();
        Map<ProductType, Boolean> map = new HashMap<>();
        types.forEach(type -> {
            map.put(type, productRepository.findByProductType(type).isEmpty());
        });
        model.addAttribute("map", map);
        return "productTypeList";
    }

    @GetMapping("productTypeList/add")
    public String productTypeListAdd(Model model) {
        ProductType productType = new ProductType();
        model.addAttribute("productType", productType);
        return "productTypeForm";
    }

    @PostMapping("productTypeList/add")
    public String productTypeListAddSubmit(@ModelAttribute ProductType productType, Model model){
        productTypeRepository.save(productType);
        return productTypeList(model);
    }

    @GetMapping("/productTypeList/delete/{productTypeId}")
    public String productTypeListDelete(@PathVariable("productTypeId") long id, Model model) {
        productTypeRepository.deleteById(id);
        return productTypeList(model);
    }

    @GetMapping("/productTypeList/edit/{productTypeId}")
    public String productTypeListEdit(@PathVariable("productTypeId") long id, Model model) {
        ProductType productType = productTypeRepository.findById(id).orElse(null);
        model.addAttribute("productType", productType);
        return "productTypeForm";
    }

    @GetMapping("/orderList")
    public String orderList(Model model) {
        Iterable<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("orderList/add")
    public String orderListAdd(Model model) {
        Order order = new Order();
        model.addAttribute("order", order);
        return "orderForm";
    }

    @PostMapping("orderList/add")
    public String orderListAddSubmit(@ModelAttribute Order order, Model model){
        orderRepository.save(order);
        return orderList(model);
    }

    @GetMapping("/orderList/delete/{orderId}")
    public String orderListDelete(@PathVariable("orderId") long id, Model model) {
        orderRepository.deleteById(id);
        return orderList(model);
    }

    @GetMapping("/orderList/edit/{orderId}")
    public String orderListEdit(@PathVariable("orderId") long id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        model.addAttribute("order", order);
        return "orderForm";
    }

    @GetMapping("/product/delete/{basketId}")
    public String basketDelete(@PathVariable("basketId") long id, Model model){
        basketRepository.deleteById(id);
        return basketList(model);
    }
}