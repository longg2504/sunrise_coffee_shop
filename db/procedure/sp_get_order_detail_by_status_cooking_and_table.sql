CREATE PROCEDURE `sp_get_order_detail_by_status_cooking_and_table`(
    IN tableId BIGINT
)
BEGIN
SELECT
    od.id AS orderDetailId,
    t.title AS tableName,
    pd.id AS productId,
    pd.title AS productTitle,
    od.note AS note,
    od.quantity AS quantity,
    u.title AS unitTitle,
    od.updated_at AS updatedAt
FROM order_detail AS od
         JOIN products AS pd
              ON od.product_id = pd.id
         JOIN units AS u
              ON u.id = pd.unit_id
         JOIN orders AS o
              ON od.order_id = o.id
         JOIN tables As t
              ON o.table_order_id = t.id
WHERE od.status = 'COOKING'
  AND o.table_order_id = tableId
ORDER BY od.updated_at ASC ;

END
