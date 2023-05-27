ALTER TABLE T_REL_BAUSTEIN_ELEMENT ADD ANZEIGE_ORDER NUMBER(19,0) default 0 not null;

merge into T_REL_BAUSTEIN_ELEMENT m
using (
select row_number() over(partition by KB_STAND_REF_UUID order by ELEMENT_NR asc) rn,
rowid as the_rowid
from (
select KB_STAND_REF_UUID,E.ELEMENT_NR
from T_REL_BAUSTEIN_ELEMENT BE 
join T_ELEMENTVERSION EV on (BE.KE_VERSION_REF_UUID=EV.UUIDKEVERSION)
join T_ELEMENT E on (EV.KE_REF_UU_ID=E.UUIDKE)
)temp) m1
on
 (
 m.rowid = m1.the_rowid
)
when matched then update set m.ANZEIGE_ORDER = m1.rn;