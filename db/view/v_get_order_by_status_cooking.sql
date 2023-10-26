SELECT
    od.id AS id,
    od.total_amount AS totalAmount,
    od.paid AS orderStatus,
    od.table_order_id AS tableId,
    t.title AS tableName
FROM orders AS od
JOIN tables AS t
ON od.table_order_id = t.id
WHERE od.deleted = false
AND od.paid = false
;