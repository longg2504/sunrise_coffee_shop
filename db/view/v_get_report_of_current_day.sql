SELECT COALESCE(SUM(b.total_amount), 0) AS totalAmount,
       COUNT(b.id) AS count
FROM
    bills b
WHERE
    ((b.created_at = NOW())
  AND (b.paid = TRUE))