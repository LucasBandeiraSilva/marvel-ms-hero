INSERT INTO tb_agent (id, agent_code, name, active) values ('a150b8b6-1be5-456c-9dfc-7327f4400dae','Shield-001','Nick Fury',TRUE);

INSERT INTO tb_hero (id, hero_code, name, skills, age, characteristics, agent_id) VALUES
  ('187bc50e-b503-4034-9ba1-b1dfd2da3b25', 'Marvel-002', 'Winter Soldier', ARRAY['Expert marksmanship', 'Hand-to-hand combat', 'Tactical strategy'], 38, ARRAY['Vengeful', 'Relentless', 'Strategic'], 'a150b8b6-1be5-456c-9dfc-7327f4400dae');

INSERT INTO tb_hero (id, hero_code, name, skills, age, characteristics, agent_id) VALUES
  ('82fde130-62a7-4270-8028-ba7b18022f25', 'Marvel-003', 'Winter Soldier', ARRAY['Super strength', 'Cybernetic arm', 'Stealth'], 39, ARRAY['Conflicted', 'Loyal', 'Stoic'], 'a150b8b6-1be5-456c-9dfc-7327f4400dae');

INSERT INTO tb_hero (id, hero_code, name, skills, age, characteristics, agent_id) VALUES
  ('6ca12da1-b597-4747-9c54-f7c133ade8de', 'Marvel-004', 'Magneto',
    ARRAY['Magnetism manipulation', 'Genius-level intellect'],
    60,
    ARRAY['Militant', 'Powerful', 'Charismatic'],
    'a150b8b6-1be5-456c-9dfc-7327f4400dae');
