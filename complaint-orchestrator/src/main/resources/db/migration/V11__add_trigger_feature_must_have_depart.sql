CREATE OR REPLACE FUNCTION check_depart_deletable()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM feature_depart fd
        WHERE fd.depart_id = OLD.id
          AND (
              SELECT COUNT(*)
              FROM feature_depart fd2
              WHERE fd2.feature_id = fd.feature_id
          ) = 1
    ) THEN
        RAISE EXCEPTION 'depart(id=%)를 삭제할 수 없습니다. 연결된 feature 중 다른 depart가 없는 항목이 있습니다.', OLD.id;
END IF;
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_depart_deletable
    BEFORE DELETE ON depart
    FOR EACH ROW EXECUTE FUNCTION check_depart_deletable();