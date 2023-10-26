SELECT
    pd.id AS productId,
    pd.title AS productTitle,
    od.note AS note,
    SUM(od.count) AS count,
    SUM(od.quantity) AS quantity,
    u.title AS unitTitle
FROM order_detail AS od
JOIN products AS pd
ON od.product_id = pd.id
JOIN units AS u
on pd.unit_id = u.id
WHERE od.status = "COOKING"
GROUP BY od.product_id, od.note
;
