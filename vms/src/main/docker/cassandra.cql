# 1) Log into docker container
# docker exec -it column-db /bin/bash
# 2) Run CQL
# $ cqlsh localhost

create keyspace if not exists research with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};

create table if not exists research.sources(uri text, creationTime text, name text, description text, protocol text, url text, domain text, primary key (uri));
create index on research.sources (creationTime);
create index on research.sources (name);
create index on research.sources (protocol);
create index on research.sources (url);
create index on research.sources (domain);

create table if not exists research.domains(uri text, creationTime text, name text, description text, primary key (uri));
create index on research.domains (creationTime);
create index on research.domains (name);

create table if not exists research.documents(uri text, creationTime text, publishedOn text, publishedBy text, authoredOn text, authoredBy text, contributedBy text, retrievedBy text, retrievedFrom text, retrievedOn text, format text, language text, title text, subject text, description text, rights text, type text, content text, tokens text, primary key (uri));
create index on research.documents (creationTime);
create index on research.documents (publishedOn);
create index on research.documents (publishedBy);
create index on research.documents (authoredOn);
create index on research.documents (authoredBy);
create index on research.documents (contributedBy);
create index on research.documents (retrievedBy);
create index on research.documents (retrievedFrom);
create index on research.documents (retrievedOn);
create index on research.documents (format);
create index on research.documents (language);
create index on research.documents (title);
create index on research.documents (subject);
create index on research.documents (rights);
create index on research.documents (type);

create table if not exists research.items(uri text, creationTime text, authoredOn text, authoredBy text, contributedBy text, format text, language text, title text, subject text, description text, url text, type text, content text, tokens text, primary key (uri));
create index on research.items (creationTime);
create index on research.items (authoredOn);
create index on research.items (authoredBy);
create index on research.items (contributedBy);
create index on research.items (format);
create index on research.items (language);
create index on research.items (title);
create index on research.items (subject);
create index on research.items (url);
create index on research.items (type);

create table if not exists research.parts(uri text, creationTime text, sense text, content text, tokens text, primary key (uri));
create index on research.parts (creationTime);
create index on research.parts (sense);

create table if not exists research.words(uri text, creationTime text, content text, lemma text, stem text, pos text, type text, primary key (uri));
create index on research.words (creationTime);
create index on research.words (content);
create index on research.words (lemma);
create index on research.words (stem);
create index on research.words (pos);
create index on research.words (type);

create table if not exists research.topics(uri text, creationTime text, content text, analysis text, primary key (uri));
create index on research.topics (creationTime);
create index on research.topics (analysis);

create table if not exists research.relations(uri text, creationTime text, type text, describes text, content text, analysis text, primary key (uri));
create index on research.relations (creationTime);
create index on research.relations (type);
create index on research.relations (describes);
create index on research.relations (analysis);

create table if not exists research.analyses(uri text, creationTime text, type text, description text, configuration text, domain text, primary key (uri));
create index on research.analyses (creationTime);
create index on research.analyses (type);
create index on research.analyses (configuration);
create index on research.analyses (domain);