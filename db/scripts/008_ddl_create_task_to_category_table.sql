CREATE TABLE task_to_category (
id serial PRIMARY KEY,
task_id int not NULL REFERENCES tasks(id),
category_id int not NULL REFERENCES categories(id),
UNIQUE(task_id, category_id)
)