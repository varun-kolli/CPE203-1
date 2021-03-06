final class Action
{

   private ActionKind kind;
   private Entity entity;
   private WorldModel world;
   private ImageStore imageStore;
   private int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeAction(EventScheduler scheduler)
   {
      switch (this.kind)
      {
         case ACTIVITY:
            executeActivityAction(scheduler);
            break;

         case ANIMATION:
            executeAnimationAction(scheduler);
            break;
      }
   }

   private void executeAnimationAction(EventScheduler scheduler)
   {
      this.entity.nextImage();

      if (this.repeatCount != 1)
      {
         scheduler.scheduleEvent(this.entity,
                 createAnimationAction(this.entity, Math.max(this.repeatCount - 1, 0)),
                 this.entity.getAnimationPeriod());
      }
   }

   private void executeActivityAction(EventScheduler scheduler)
   {
      switch (this.entity.kind())
      {
         case MINER_FULL:
            this.entity.executeMinerFullActivity(this.world,
                    this.imageStore, scheduler);
            break;

         case MINER_NOT_FULL:
            this.entity.executeMinerNotFullActivity(this.world,
                    this.imageStore, scheduler);
            break;

         case ORE:
            this.entity.executeOreActivity(this.world, this.imageStore,
                    scheduler);
            break;

         case ORE_BLOB:
            this.entity.executeOreBlobActivity(this.world,
                    this.imageStore, scheduler);
            break;

         case QUAKE:
            this.entity.executeQuakeActivity(this.world, this.imageStore,
                    scheduler);
            break;

         case VEIN:
            this.entity.executeVeinActivity(this.world, this.imageStore,
                    scheduler);
            break;

         default:
            throw new UnsupportedOperationException(
                    String.format("executeActivityAction not supported for %s",
                            this.entity.kind()));
      }
   }

   public static Action createAnimationAction(Entity entity, int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
   }


   public static Action createActivityAction(Entity entity, WorldModel world,
                                             ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
   }
}
