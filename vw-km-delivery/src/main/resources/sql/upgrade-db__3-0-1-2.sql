ALTER TABLE T_ELEMENT ADD ANZEIGE_ORDER NUMBER(19,0) default 0 not null;
ALTER TABLE T_BAUSTEIN ADD ANZEIGE_ORDER NUMBER(19,0) default 0 not null;


merge into T_ELEMENT m
using (
select row_number() over(partition by ORDNER_REF_ID order by ELEMENT_NR asc,BEZEICHNUNG asc) rn,
rowid as the_rowid
from T_ELEMENT) m1
on
 (
 m.rowid = m1.the_rowid
)
when matched then update set m.ANZEIGE_ORDER = m1.rn;


merge into T_BAUSTEIN m
using (
select row_number() over(partition by ORDNER_REF_ID order by BAUSTEIN_NR asc,BEZEICHNUNG asc) rn,
rowid as the_rowid
from T_BAUSTEIN) m1
on
 (
 m.rowid = m1.the_rowid
)
when matched then update set m.ANZEIGE_ORDER = m1.rn;