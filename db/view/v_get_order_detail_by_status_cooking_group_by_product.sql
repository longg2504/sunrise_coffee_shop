SELECT
    od.id AS orderDetailId,
    pd.id AS productId,
    pd.title AS productTitle,
    od.note AS note,
    od.status AS status,
    t.title AS tableName,
    SUM(od.quantity) AS quantity,
    u.title AS unitTitle
FROM order_detail od
     JOIN products pd ON od.product_id = pd.id
     JOIN units u ON pd.unit_id = u.id
     JOIN orders o ON o.id = od.order_id
     JOIN tables t ON o.table_order_id = t.id
WHERE
    (od.status = 'COOKING')
GROUP BY od.id , pd.id , pd.title , od.note , od.status , u.title
;
