SELECT
    COALESCE(SUM(b.total_amount), 0) AS totalAmount,
    COUNT(b.id) AS count
FROM
    bills b
WHERE
    ((MONTH(b.created_at) = MONTH(NOW()))
        AND (YEAR(b.created_at) = YEAR(NOW()))
        AND (b.paid = TRUE))