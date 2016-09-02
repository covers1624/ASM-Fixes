list potionFix
ALOAD 0
ALOAD 1
INVOKESTATIC covers1624/asmfixes/asm/ASMHooks.potionOnUpdateHook(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/entity/EntityLivingBase;)Z
IRETURN

list n_numinaLogSpam
LDC *
INVOKESTATIC net/machinemuse/numina/general/MuseLogger.logError (Ljava/lang/String;)Lscala/None$;
POP

list i_preRenderTile
ALOAD 0
INVOKESTATIC covers1624/asmfixes/asm/ASMHooksClient.preRenderTile(Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;)V

list i_postRenderTile
INVOKESTATIC covers1624/asmfixes/asm/ASMHooksClient.postRenderTile()V
