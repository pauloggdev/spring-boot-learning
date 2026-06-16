package api.algorithm_engine.dto;

public record CreateOrderRequest(String symbol, String side, Integer quantity) {}
