--insert문 추가 후 해당 파일 이름 앞에 V6__를 붙일 것

--여기에 insert문

CREATE RULE no_insert_feature AS ON INSERT TO feature DO INSTEAD NOTHING;
CREATE RULE no_update_feature AS ON UPDATE TO feature DO INSTEAD NOTHING;
CREATE RULE no_delete_feature AS ON DELETE TO feature DO INSTEAD NOTHING;