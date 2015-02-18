# select * from groups
$in = <<EOF;
"kisee";"rm-oper";1234
"tomcat";"manager";1235
"3146";"rm-oper";1236
"kristal";"rm-oper";1237
"egorb";"rm-oper";1240
"reshet";"rm-oper";1242
"kesvv";"rm-oper";1243
"koprovam";"rm-oper";1248
"kosos";"rm-oper";1249
"sorokinan";"rm-oper";1250
"regfizlic";"rm-oper";1251
"vana";"rm-oper";1252
"lyubaa";"rm-oper";1253
"starkoval";"rm-oper";1254
"kirus";"rm-oper";1256
"denisovav";"rm-oper";1257
"razsv";"rm-oper";1258
"muhap";"rm-oper";1259
"smolenovas";"rm-oper";1260
"romanovan";"rm-oper";1262
"line6";"rm-oper";1263
"rybinsk";"rm-oper";1264
"golcivaea";"rm-oper";1265
"morozovaon";"rm-oper";1266
"balinaen";"rm-oper";1268
"portnov";"rm-oper";1269
"romanovan";"rm-admin";1274
"uglich";"rm-oper";1275
"feder";"rm-oper";1276
"zahar";"rm-oper";1277
"rumy";"rm-oper";1278
"fedlv";"rm-oper";1279
"tutaev";"rm-oper";1280
"nekouz";"rm-oper";1281
"danilov";"rm-oper";1282
"smirmar";"rm-oper";1283
"selivanov";"rm-oper";1284
"ekatp";"rm-oper";1285
"pestova";"rm-oper";1267
"dmitriev";"rm-oper";0
"gerasimov";"rm-oper";0
"kvaskov";"rm-oper";0
"leys";"rm-oper";0
"butovskaya";"rm-oper";0
"smakotina";"rm-oper";0
"tsarev";"rm-oper";0
"tomcat";"rm-oper";0
"gerdo";"rm-oper";0
"demay";"rm-oper";0
"buxtiyarov";"rm-oper";0
"ira";"rm-oper
"polina";"rm-oper";0
"lybava";"rm-oper";0
"mari";"rm-oper";0
"nastya";"rm-oper";0
"elena";"rm-oper";0
"alexeym";"rm-oper";0
"alexeym";"rm-admin";1
"2631";"rm-oper";0
"elenab";"rm-oper";0
"kuligina";"rm-oper";0
"2906";"rm-oper";0
"2906";"rm-admin";1
"alexeyp";"rm-oper";0
"alexeyp";"rm-admin";1
"magistr";"rm-oper";0
"magistr";"rm-admin";1
"kuligina";"rm-admin";1
"sveta";"rm-oper";0
"fedorov";"rm-oper";0
"dormakov";"rm-oper";0
"bagrov";"rm-oper";0
"dorvb";"rm-oper";0
"sipugin";"rm-oper";0
"baranov";"rm-oper";0
"veksin";"rm-oper";0
"alesha";"rm-oper";0
"pankrat";"rm-oper";0
"sivova";"rm-oper";0
"danilovsn";"rm-oper";0
"kogin";"rm-oper";0
"mishag";"rm-oper";0
"annac";"rm-oper";0
"smirnov";"rm-oper";0
"vilmov";"rm-oper";0
"vilmov";"rm-admin";1
"vilmov";"full";2
EOF

%userGroup = ();

for my $line (split "\n",$in) {
	my ($user,$group) = split ';', $line;
	$user =~ s/"//g;
	$group =~ s/"//g;
	my $sorter = 0;
	if($userGroup{"$user"}) {
		$sorter = $userGroup{"$user"};
	}
	print <<EOF;
insert into groups (login,role,sorter) values ('$user','$group',$sorter);
EOF
	$userGroup{"$user"}++;
}
