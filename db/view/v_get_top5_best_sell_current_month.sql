SELECT p.id,
       p.title,
       pa.file_folder   AS fileFolder,
       pa.file_name     AS fileName,
       SUM(od.quantity) AS quantity,
       SUM(od.amount)   AS amount
FROM products AS p
         LEFT JOIN order_detail AS od
                   ON p.id = od.product_id
         JOIN avatars AS pa
              ON p.product_avatar_id = pa.id
WHERE (
    MONTH(od.created_at) = MONTH (NOW())
    OR od.created_at IS NULL
    )
  AND (
    YEAR(od.created_at) = YEAR (NOW())
    OR od.created_at IS NULL
    )
  AND (od.status <> 'COOKING'
    OR od.status IS NULL)
  AND p.deleted = false
GROUP BY p.id
ORDER BY SUM(od.quantity) DESC LIMIT 5