# select * from action_object_type
$in = <<EOF;
2;2;0;""
3;2;0;""
6;4;0;""
7;4;0;""
8;4;0;""
9;3;0;""
674;1;0;""
674;2;1;""
693;681;0;""
694;682;0;""
683;680;0;""
3135;680;0;""
4;2;0;"on"
5;2;0;"on"
5;1;1;"on"
4;1;1;"on"
1;2;0;"on"
1;1;1;"on"
3268;4;0;""
EOF

%generatedObjTypeItem = ();
$generatedObjTypeItemId=0;

for my $line (split "\n",$in) {
	my ($act,$type,$sorter,$req) = split ';',$line;
	$req =~ s/\"/\'/g;
	if(! $generatedObjTypeItem{"$type-$req"}) {
		++$generatedObjTypeItemId;
		print <<EOF;
insert into object_type_item (id,type_id,required) values ($generatedObjTypeItemId,$type,$req);
EOF
		$generatedObjTypeItem{"$type-$req"} = $generatedObjTypeItemId;
	} 
	my $currentTypeId = $generatedObjTypeItem{"$type-$req"};
	print <<EOF;
insert into action_object_type_item (action_id,object_type_item_id,sorter) values ($act,$currentTypeId,$sorter);
EOF

}