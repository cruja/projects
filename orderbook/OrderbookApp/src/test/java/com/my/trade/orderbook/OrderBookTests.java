package com.my.trade.orderbook;

import com.my.trade.orderbook.events.*;
import org.trading.orderbook.gui.model.TableDataModel;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.book.OrderBookImpl;
import org.trading.orderbook.model.impl.exception.OrderIdAlreadyExistsException;
import org.trading.orderbook.model.impl.level.OrdersPerLevel;
import org.trading.orderbook.model.impl.side.OrdersPerSide;
import org.trading.orderbook.util.Utils;
import org.trading.orderbook.model.impl.DefaultModelObserver;
import org.trading.orderbook.model.impl.Order;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrderBookTests {

    @Test
    public void testOrderBook1() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver onFillModelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(onFillModelObserver);

        book.add(new Order(1, "book-1", true, 10, 100));
        book.add(new Order(2, "book-1", true, 10, 100));

        assertTrue(book.getLowestPrice(true) == 10.0);
        assertTrue(book.getHighestPrice(true) == 10);
        assertTrue(book.getBestPrice(true) == 10);

        assertTrue(Double.isNaN(book.getLowestPrice(false)));
        assertTrue(Double.isNaN(book.getHighestPrice(false)));
        assertTrue(Double.isNaN(book.getBestPrice(false)));

        assertTrue(book.getAccVol(true) == 200);
        assertTrue(book.getAccVol(false) == 0);

        assertTrue(book.getOrdersCount(true) == 2);
        assertTrue(book.getOrdersCount(false) == 0);

        assertTrue(book.getOrdersCount(10, true) == 2);

        assertTrue(book.getAccVolume(10, true) == 200);

        assertTrue(fills.equals(Collections.<Integer>emptyList()));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook2() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", true, 10, 100));
        book.add(new Order(2, "book-1", false, 20, 100));


        assertTrue(book.getLowestPrice(true) == new Double(10.0));
        assertTrue(book.getHighestPrice(true) == 10);
        assertTrue(book.getBestPrice(true) == 10);

        assertTrue(book.getLowestPrice(false) == 20);
        assertTrue(book.getHighestPrice(false) == 20);
        assertTrue(book.getBestPrice(false) == 20);

        assertTrue(book.getAccVol(true) == 100);
        assertTrue(book.getAccVol(false) == 100);

        assertTrue(book.getOrdersCount(true) == 1);
        assertTrue(book.getOrdersCount(false) == 1);

        assertTrue(book.getOrdersCount(10, true) == 1);
        assertTrue(book.getOrdersCount(20, false) == 1);

        assertTrue(book.getAccVolume(10, true) == 100);
        assertTrue(book.getAccVolume(20, false) == 100);

        assertTrue(fills.equals(Collections.<Integer>emptyList()));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook3() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", true, 20, 10));
        book.add(new Order(2, "book-1", false, 10, 100));


        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(book.getLowestPrice(false) == 10);
        assertTrue(book.getHighestPrice(false) == 10);
        assertTrue(book.getBestPrice(false) == 10);

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 90);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 1);

        assertTrue(book.getOrdersCount(20, true) == 0);
        assertTrue(book.getOrdersCount(10, false) == 1);

        assertTrue(book.getAccVolume(20, true) == 0);
        assertTrue(book.getAccVolume(10, false) == 90);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(10);
            }
        };
        assertTrue(fills.equals(expectedFills));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(2L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook4() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", false, 10, 100));
        book.add(new Order(2, "book-1", true, 20, 10));


        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(book.getLowestPrice(false) == 10);
        assertTrue(book.getHighestPrice(false) == 10);
        assertTrue(book.getBestPrice(false) == 10);

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 90);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 1);

        assertTrue(book.getOrdersCount(20, true) == 0);
        assertTrue(book.getOrdersCount(10, false) == 1);

        assertTrue(book.getAccVolume(20, true) == 0);
        assertTrue(book.getAccVolume(10, false) == 90);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(10);
            }
        };
        assertTrue(fills.equals(expectedFills));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook5() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", true, 10, 100));
        book.add(new Order(2, "book-1", false, 10, 100));

        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(Double.isNaN(book.getLowestPrice(false)));
        assertTrue(Double.isNaN(book.getHighestPrice(false)));
        assertTrue(Double.isNaN(book.getBestPrice(false)));

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 0);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 0);

        assertTrue(book.getOrdersCount(10, true) == 0);
        assertTrue(book.getOrdersCount(10, false) == 0);

        assertTrue(book.getAccVolume(10, true) == 0);
        assertTrue(book.getAccVolume(10, false) == 0);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(100);
            }
        };
        assertTrue(fills.equals(expectedFills));

        assertTrue(book.getOrderIds().equals(Collections.<Long>emptyList()));
    }

    @Test
    public void testOrderBook6() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {
            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", false, 10, 100));
        book.add(new Order(2, "book-1", false, 9, 100));
        book.add(new Order(3, "book-1", true, 8, 10));
        book.add(new Order(4, "book-1", true, 11, 10));
        book.add(new Order(5, "book-1", true, 10, 10));

        assertTrue(book.getLowestPrice(true) == 8.0d);
        assertTrue(book.getHighestPrice(true) == 8.0d);
        assertTrue(book.getBestPrice(true) == 8.0d);

        assertTrue(book.getAccVol(true) == 10);
        assertTrue(book.getAccVol(false) == 180);

        assertTrue(book.getOrdersCount(true) == 1);
        assertTrue(book.getOrdersCount(false) == 2);

        assertTrue(book.getOrdersCount(8, true) == 1);
        assertTrue(book.getOrdersCount(10, true) == 0);
        assertTrue(book.getOrdersCount(11, true) == 0);

        assertTrue(book.getOrdersCount(9, false) == 1);
        assertTrue(book.getOrdersCount(10, false) == 1);

        assertTrue(book.getAccVolume(8, true) == 10);
        assertTrue(book.getAccVolume(9, false) == 80);
        assertTrue(book.getAccVolume(10, false) == 100);

        assertTrue(book.getLowestPrice(false) == 9.0d);
        assertTrue(book.getHighestPrice(false) == 10.0d);
        assertTrue(book.getBestPrice(false) == 9.0d);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(10);
                add(10);
            }
        };
        assertTrue(fills.equals(expectedFills));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
                add(3L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook7() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {
            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        Order order_1 = new Order(1, "book-1", false, 12, 1);
        Order order_2 = new Order(2, "book-1", false, 11, 2);
        Order order_3 = new Order(3, "book-1", false, 10, 1);
        Order order_4 = new Order(4, "book-1", true, 11, 2);
        Order order_5 = new Order(5, "book-1", true, 10, 1);

        book.add(order_1);
        book.add(order_2);
        book.add(order_3);
        book.add(order_4);
        book.delete(order_2);
        book.add(order_5);

        assertTrue(book.getLowestPrice(true) == 10.0d);
        assertTrue(book.getHighestPrice(true) == 10.0d);
        assertTrue(book.getBestPrice(true) == 10.0d);

        assertTrue(book.getAccVol(true) == 1);
        assertTrue(book.getAccVol(false) == 1);

        assertTrue(book.getOrdersCount(true) == 1);
        assertTrue(book.getOrdersCount(false) == 1);

        assertTrue(book.getOrdersCount(10, true) == 1);
        assertTrue(book.getOrdersCount(11, true) == 0);

        assertTrue(book.getOrdersCount(12, false) == 1);
        assertTrue(book.getOrdersCount(11, false) == 0);
        assertTrue(book.getOrdersCount(10, false) == 0);

        assertTrue(book.getAccVolume(10, true) == 1);
        assertTrue(book.getAccVolume(12, false) == 1);
        assertTrue(book.getAccVolume(11, false) == 0);
        assertTrue(book.getAccVolume(10, false) == 0);

        assertTrue(book.getLowestPrice(false) == 12.0d);
        assertTrue(book.getHighestPrice(false) == 12.0d);
        assertTrue(book.getBestPrice(false) == 12.0d);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(2);
            }
        };
        assertTrue(fills.equals(expectedFills));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(5L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook8() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Event> events = new ArrayList<Event>();

        IModelObserver modelObserver = new IModelObserver() {

            @Override
            public void onAdded(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
                events.add(new OnAddedEvent(sellOrdCnt, sellAccVol, price, buyAccVol, buyOrdCnt));
            }

            @Override
            public void onPriceRemoved(double price) {
                events.add(new OnPriceRemovedEvent(price));
            }

            @Override
            public void onUpdated(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
                events.add(new OnUpdatedEvent(sellOrdCnt, sellAccVol, price, buyAccVol, buyOrdCnt));
            }

            @Override
            public void onUpdate(OrdersPerSide orderSide, double price) {
                events.add(new OnUpdateEvent(orderSide, price));
            }

            @Override
            public void onChanged(OrdersPerSide side, double price) {

            }

            @Override
            public void onOrderAdded(long orderId, int size) {
                events.add(new OrderAddedEvent(orderId));
            }

            @Override
            public void onOrderFilled(long orderId, int volFilled) {
                events.add(new OrderFilledEvent(orderId, volFilled));
            }

            @Override
            public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
                events.add(new OrderPartiallyFilledEvent(orderId, volFilled, volRemained));
            }

            @Override
            public void onOrderDeleted(long orderId) {
                events.add(new OrderDeletedEvent(orderId));
            }

            @Override
            public void onFilled(int volume) {
                events.add(new FilledEvent(volume));
            }
        };
        book.register(modelObserver);

        Order order_1 = new Order(1, "book-1", false, 12, 1);
        Order order_2 = new Order(2, "book-1", false, 11, 2);
        Order order_3 = new Order(3, "book-1", false, 10, 1);
        Order order_4 = new Order(4, "book-1", true, 11, 2);
        Order order_5 = new Order(5, "book-1", true, 10, 1);

        book.add(order_1);
        book.add(order_2);
        book.add(order_3);
        book.add(order_4);
        book.delete(order_2);
        book.add(order_5);

        List<Event> expectedEvents = new ArrayList<Event>() {
            {
                add(new OrderAddedEvent(1));
                add(new OrderAddedEvent(2));
                add(new OrderAddedEvent(3));
                add(new OrderFilledEvent(3, 1));
                add(new OrderPartiallyFilledEvent(2, 1, 1));
                add(new OrderFilledEvent(4, 2));
                add(new FilledEvent(2));
                add(new OrderDeletedEvent(4));
                add(new OrderDeletedEvent(2));
                add(new OrderAddedEvent(5));

            }
        };
        assertTrue(events.equals(expectedEvents));
    }

    @Test
    public void testOrderBook9() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", false, 10, 10));
        book.add(new Order(2, "book-1", true, 11, 5));
        book.add(new Order(3, "book-1", false, 12, 10));

        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(book.getLowestPrice(false) == 10.0);
        assertTrue(book.getHighestPrice(false) == 12);
        assertTrue(book.getBestPrice(false) == 10);

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 15);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 2);

        assertTrue(book.getAccVolume(10, true) == 0);
        assertTrue(book.getAccVolume(11, true) == 0);
        assertTrue(book.getAccVolume(12, true) == 0);

        assertTrue(book.getAccVolume(10, false) == 5);
        assertTrue(book.getAccVolume(12, false) == 10);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(5);
            }
        };
        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(3L);
            }
        };
        assertTrue(fills.equals(expectedFills));

        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testOrderBook10() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {

            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        book.add(new Order(1, "book-1", true, 10, 10));
        book.add(new Order(2, "book-1", false, 11, 5));
        book.add(new Order(3, "book-1", true, 12, 10));

        assertTrue(Double.isNaN(book.getLowestPrice(false)));
        assertTrue(Double.isNaN(book.getHighestPrice(false)));
        assertTrue(Double.isNaN(book.getBestPrice(false)));

        assertTrue(book.getLowestPrice(true) == 10.0);
        assertTrue(book.getHighestPrice(true) == 12.0);
        assertTrue(book.getBestPrice(true) == 12.0);

        assertTrue(book.getAccVol(false) == 0);
        assertTrue(book.getAccVol(true) == 15);

        assertTrue(book.getOrdersCount(false) == 0);
        assertTrue(book.getOrdersCount(true) == 2);

        assertTrue(book.getAccVolume(10, false) == 0);
        assertTrue(book.getAccVolume(11, false) == 0);
        assertTrue(book.getAccVolume(12, false) == 0);

        assertTrue(book.getAccVolume(10, true) == 10);
        assertTrue(book.getAccVolume(12, true) == 5);

        List<Integer> expectedFills = new ArrayList<Integer>() {
            {
                add(5);
            }
        };
        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
                add(3L);
            }
        };
        assertTrue(fills.equals(expectedFills));

        assertTrue(book.getOrderIds().equals(expectedOrderIds));
    }

    @Test
    public void testAddOrderWithIdUsed() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        Order order = new Order(1, "book-1", false, 10, 1);
        book.add(order);

        try {
            book.add(order);
            assertTrue("An exception was supposed to be thrown", false);
        } catch (Exception e) {
            assertTrue(e instanceof OrderIdAlreadyExistsException);
        }
    }

    @Test
    public void testAddDeleteAddSameOrder() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        Order order = new Order(1, "book-1", false, 10, 1);
        try {
            book.add(order);
            book.delete(order);
            book.add(order);
        } catch (Throwable e) {
            assertTrue("Nothing was supposed to be thrown", false);
        }
    }

    @Test
    public void testDeleteAlreadyFilledOrder1() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        Order order_1 = new Order(1, "book-1", false, 10, 1);
        Order order_2 = new Order(2, "book-1", true, 11, 1);

        try {
            book.add(order_1);
            book.add(order_2);
            book.delete(order_1);
            book.delete(order_2);
        } catch (Throwable e) {
            assertTrue("Nothing was supposed to be thrown", false);
        }
    }

    @Test
    public void testDeleteAlreadyFilledOrder2() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        Order[] orders = new Order[]{
                new Order(1, "book-1", false, 14, 2),
                new Order(2, "book-1", false, 13, 1),
                new Order(3, "book-1", false, 12, 1),
                new Order(4, "book-1", false, 11, 1),
                new Order(5, "book-1", false, 10, 1),
                new Order(6, "book-1", true, 14, 5)
        };


        for (Order order : orders) {
            book.add(order);
        }

        for (int i = 1; i < 6; i++) {
            try {
                book.delete(orders[i]);
            } catch (Throwable e) {
                assertTrue("Nothing was supposed to be thrown", false);
            }
        }
    }

    @Test
    public void testAggregators() {
        OrderBookImpl book = new OrderBookImpl("book-1");

        final List<Integer> fills = new ArrayList<Integer>();
        IModelObserver modelObserver = new DefaultModelObserver() {
            @Override
            public void onFilled(int volume) {
                fills.add(volume);
            }
        };
        book.register(modelObserver);

        Order order = new Order(1, "book-1", false, 10, 1);
        book.add(order);

        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(book.getLowestPrice(false) == 10.0d);
        assertTrue(book.getHighestPrice(false) == 10.0d);
        assertTrue(book.getBestPrice(false) == 10.0d);

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 1);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 1);

        assertTrue(book.getOrdersCount(10, true) == 0);
        assertTrue(book.getOrdersCount(10, false) == 1);

        assertTrue(book.getAccVolume(10, false) == 1);

        assertTrue(fills.equals(Collections.<Integer>emptyList()));

        ArrayList<Long> expectedOrderIds = new ArrayList<Long>() {
            {
                add(1L);
            }
        };
        assertTrue(book.getOrderIds().equals(expectedOrderIds));


        book.delete(order);

        assertTrue(Double.isNaN(book.getLowestPrice(true)));
        assertTrue(Double.isNaN(book.getHighestPrice(true)));
        assertTrue(Double.isNaN(book.getBestPrice(true)));

        assertTrue(Double.isNaN(book.getLowestPrice(false)));
        assertTrue(Double.isNaN(book.getHighestPrice(false)));
        assertTrue(Double.isNaN(book.getBestPrice(false)));

        assertTrue(book.getAccVol(true) == 0);
        assertTrue(book.getAccVol(false) == 0);

        assertTrue(book.getOrdersCount(true) == 0);
        assertTrue(book.getOrdersCount(false) == 0);

        assertTrue(book.getOrdersCount(10, true) == 0);
        assertTrue(book.getOrdersCount(10, false) == 0);

        assertTrue(book.getAccVolume(10, true) == 0);
        assertTrue(book.getAccVolume(10, false) == 0);

        assertTrue(fills.equals(Collections.<Integer>emptyList()));

        assertTrue(book.getOrderIds().equals(Collections.<Long>emptyList()));
    }

    @Test
    public void testUtils() {
        List<Double> list = new ArrayList<Double>();
        assertTrue(Utils.indexForPrice(10.0, list) == -1);
        assertTrue(Utils.potentialIndexForPrice(10.0, list) == 0);

        list.add(10.0);
        assertTrue(Utils.indexForPrice(10.0, list) == 0);
        assertTrue(Utils.potentialIndexForPrice(9.5, list) == 0);
        assertTrue(Utils.potentialIndexForPrice(10.5, list) == 1);

        list.add(11.0);
        assertTrue(Utils.indexForPrice(10.5, list) == -1);
        assertTrue(Utils.potentialIndexForPrice(9.5, list) == 0);
        assertTrue(Utils.potentialIndexForPrice(10.5, list) == 1);
        assertTrue(Utils.potentialIndexForPrice(11.5, list) == 2);

    }

    @Test
    public void testOrderBookDataModel() {
        TableDataModel dataModel = new TableDataModel();
        expect(
                dataModel,
                new ArrayList<Double>(),
                new Object[][]{}
        );

        assertTrue(!dataModel.remove(10.0d));

        dataModel.add(10.0d, 1, 100, 200, 2);
        dataModel.add(20.0d, 2, 200, 400, 4);
        dataModel.add(30.0d, 3, 300, 600, 6);
        dataModel.add(40.0d, 4, 400, 800, 8);

        assertTrue(!dataModel.add(10.0d, 1, 100, 200, 2));

        expect(
                dataModel,
                new ArrayList<Double>() {
                    {
                        add(10.0d);
                        add(20.0d);
                        add(30.0d);
                        add(40.0d);
                    }
                },
                new Object[][]{
                        new Object[]{1, 100, 200, 2},
                        new Object[]{2, 200, 400, 4},
                        new Object[]{3, 300, 600, 6},
                        new Object[]{4, 400, 800, 8},
                }
        );

        dataModel.update(30, 12, 123, 1234, 12345);

        expect(
                dataModel,
                new ArrayList<Double>() {
                    {
                        add(10.0d);
                        add(20.0d);
                        add(30.0d);
                        add(40.0d);
                    }
                },
                new Object[][]{
                        new Object[]{1, 100, 200, 2},
                        new Object[]{2, 200, 400, 4},
                        new Object[]{12, 123, 1234, 12345},
                        new Object[]{4, 400, 800, 8},
                }
        );

        assertTrue(!dataModel.remove(25.0d));

        dataModel.remove(20.0d);

        expect(
                dataModel,
                new ArrayList<Double>() {
                    {
                        add(10.0d);
                        add(30.0d);
                        add(40.0d);
                    }
                },
                new Object[][]{
                        new Object[]{1, 100, 200, 2},
                        new Object[]{12, 123, 1234, 12345},
                        new Object[]{4, 400, 800, 8},
                }
        );

        assertTrue(!dataModel.remove(20.0d));
    }

    @Test
    public void testOrdersPerLevel() {
        final List<Long> ordersToBeDeleted = new ArrayList<Long>();

        OrdersPerLevel level = new OrdersPerLevel(
                new DefaultModelObserver() {

                    @Override
                    public void onOrderDeleted(long orderId) {
                        ordersToBeDeleted.add(orderId);
                    }
                }
        );
        level.add(new Order(1, "book-1", true, 10.0d, 2));
        level.add(new Order(2, "book-1", true, 10.0d, 2));
        level.add(new Order(3, "book-1", true, 10.0d, 2));
        level.add(new Order(4, "book-1", true, 10.0d, 2));

        assertTrue(level.getOrdersCount() == 4);
        assertTrue(level.getAccumulatedVolume() == 8);

        level.fill(5);

        assertTrue(level.getOrdersCount() == 2);
        assertTrue(level.getAccumulatedVolume() == 3);
        ArrayList<Long> expectedOrdersToBeDeleted = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        assertTrue(ordersToBeDeleted.equals(expectedOrdersToBeDeleted));

        ordersToBeDeleted.clear();

        level.fill(3);

        assertTrue(level.getOrdersCount() == 0);
        assertTrue(level.getAccumulatedVolume() == 0);
        expectedOrdersToBeDeleted = new ArrayList<Long>() {
            {
                add(3L);
                add(4L);
            }
        };
        assertTrue(ordersToBeDeleted.equals(expectedOrdersToBeDeleted));
    }


    private void expect(
            TableDataModel dataModel,
            List<Double> expectedPrices,
            Object[][] expectedData) {
        assertTrue(dataModel.size() == expectedPrices.size());
        assertTrue(Arrays.asList(dataModel.getPrices()).equals(expectedPrices));
        assertTrue(Arrays.asList(dataModel.getLevelPrices()).equals(Arrays.asList(dataModel.getPrices())));

        for (int i = 0; i < expectedPrices.size(); i++) {
            Double price = expectedPrices.get(i);
            assertTrue(dataModel.getSellOrdCnt(price) == (Integer) expectedData[i][0]);
            assertTrue(dataModel.getSellAccVol(price) == (Integer) expectedData[i][1]);
            assertTrue(dataModel.getBuyAccVol(price) == (Integer) expectedData[i][2]);
            assertTrue(dataModel.getBuyOrdCnt(price) == (Integer) expectedData[i][3]);
        }
    }

}
