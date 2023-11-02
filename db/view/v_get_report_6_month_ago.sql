SELECT
    MONTH(b.created_at) AS month,
    YEAR(b.created_at) AS year,
    COALESCE SUM(b.total_amount), 0) AS totalAmount,
    COUNT(b.id) AS COUNT
FROM
    bills b
WHERE
    ((b.created_at BETWEEN CAST((CURDATE() - INTERVAL 5 MONTH) AS DATETIME) AND (CAST((CURDATE() - INTERVAL -(1) DAY) AS DATETIME) - INTERVAL 0 MINUTE))
  AND (b.paid = TRUE))
GROUP BY MONTH(b.created_at) , b.created_at
ORDER BY YEAR(b.created_at) , MONTH(b.created_at)