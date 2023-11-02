CREATE VIEW v_get_report_current_year
AS
SELECT
        MONTH (b.created_at) AS 'month',
        COALESCE (SUM (b.total_amount) , 0) AS totalAmount,
        COUNT (b.id) AS count
        FROM
        bills AS b
        WHERE
        YEAR (b.created_at) = YEAR (now())
        AND b.paid = true
        GROUP BY MONTH (b.created_at)
;