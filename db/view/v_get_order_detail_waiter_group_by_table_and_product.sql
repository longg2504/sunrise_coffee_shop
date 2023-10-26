SELECT
    od.id AS orderDetailId,
    o.table_order_id AS tableId,
    t.title AS tableName,
    od.product_id AS productId,
    pd.title AS productTitle,
    od.note AS note,
    SUM(od.count) AS count,
    SUM(od.quantity) AS quantity,
    u.title AS unitTitle,
    od.updated_at AS updatedAt,
    od.status AS status
FROM order_detail AS od
JOIN orders AS o ON o.id = od.order_id
JOIN tables AS t ON t.id = o.table_order_id
JOIN products AS pd ON od.product_id = pd.id
JOIN units AS u ON u.id = pd.unit_id
WHERE od.status = 'WAITER'
GROUP BY od.id, o.table_order_id, t.title, od.product_id, pd.title, od.note, u.title, od.updated_at, od.status
LIMIT 0, 1000;