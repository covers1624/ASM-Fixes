list potionFix
ALOAD 0
ALOAD 1
INVOKESTATIC covers1624/asmfixes/asm/ASMHooks.potionOnUpdateHook(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/entity/EntityLivingBase;)Z
IRETURN

list n_museLogSpam
LDC *
INVOKESTATIC net/machinemuse/numina/general/MuseLogger.logError (Ljava/lang/String;)Lscala/None$;
POP